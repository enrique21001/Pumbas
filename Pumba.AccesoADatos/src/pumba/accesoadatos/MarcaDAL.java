package pumba.accesoadatos;
import java.sql.*;
import java.util.*;

import pumbas.entidadesdenegocio.*;

public class MarcaDAL {
    
    static String obtenerCampos() {
        return "m.Id, m.Nombre";
    }
    
    private static String obtenerSelect(Marca pMarca) {
        String sql;
        sql = "SELECT ";
        if (pMarca.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {            
            sql += "TOP " + pMarca.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Marca m");
        return sql;
    }
    
    private static String agregarOrderBy(Marca pMarca) {
        String sql = " ORDER BY m.Id DESC";
        if (pMarca.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            sql += " LIMIT " + pMarca.getTop_aux() + " ";
        }
        return sql;
    }
      public static int crear(Marca pMarca) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "INSERT INTO Marca(Nombre) VALUES(?)";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setString(1, pMarca.getNombre());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return result;
    }
    
    public static int modificar(Marca pMarca) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) {
            sql = "UPDATE Marca SET Nombre=? WHERE Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setString(1, pMarca.getNombre());
                ps.setInt(2, pMarca.getId());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return result;
    }
    
    public static int eliminar(Marca pMarca) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) {
            sql = "DELETE FROM Marca WHERE Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pMarca.getId());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return result;
    } 
    
    static int asignarDatosResultSet(Marca pMarca, ResultSet pResultSet, int pIndex) throws Exception {
        pIndex++;
        pMarca.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pMarca.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Marca> pMarcas) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) {
            while (resultSet.next()) {
                Marca marca = new Marca(); 
                asignarDatosResultSet(marca, resultSet, 0);
                pMarcas.add(marca);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public static Marca obtenerPorId(Marca pMarca) throws Exception {
       Marca  marca = new Marca();
        ArrayList<Marca> marcas = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pMarca);
            sql += " WHERE m.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pMarca.getId());
                obtenerDatos(ps, marcas);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        if (marcas.size() > 0) {
            marca = marcas.get(0);
        }
        return marca;
    }
    
    public static ArrayList<Marca> obtenerTodos() throws Exception {
        ArrayList<Marca> marcas;
        marcas = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new Marca());
            sql += agregarOrderBy(new Marca());
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                obtenerDatos(ps, marcas);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        return marcas;
    }
    
    static void querySelect(Marca pMarca, ComunDB.utilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (pMarca.getId() > 0) {
            pUtilQuery.AgregarNumWhere(" m.Id=? ");
            if (statement != null) { 
                statement.setInt(pUtilQuery.getNumWhere(), pMarca.getId()); 
            }
        }

        if (pMarca.getNombre() != null && pMarca.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" m.Nombre LIKE ? "); 
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pMarca.getNombre() + "%"); 
            }
        }
    }
    
    public static ArrayList<Marca> buscar(Marca pMarca) throws Exception {
        ArrayList<Marca> marcas = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(pMarca);
            ComunDB comundb = new ComunDB();
            ComunDB.utilQuery utilQuery = comundb.new utilQuery(sql, null, 0); 
            querySelect(pMarca, utilQuery);
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pMarca);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pMarca, utilQuery);
                obtenerDatos(ps, marcas);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        return marcas;
    }

}

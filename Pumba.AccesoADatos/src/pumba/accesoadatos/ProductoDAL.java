
package pumba.accesoadatos;

import java.util.*;
import java.sql.*;
import pumbas.entidadesdenegocio.*; 


public class ProductoDAL { 


    
    static String obtenerCampos() {
        return "p.Id, p.IdMarca, p.Nombre, p.Descripcion, p.Precio";
    }
 
    private static String obtenerSelect(Producto pProducto) {
        String sql;
        sql = "SELECT ";
        if (pProducto.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
          
            sql += "TOP " + pProducto.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Producto p");
        return sql;
    }   
   
    private static String agregarOrderBy(Producto pProducto) {
        String sql = " ORDER BY p.Id DESC";
        if (pProducto.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            
            
            sql += " LIMIT " + pProducto.getTop_aux() + " ";
        }
        return sql;
    }
   
    
   
    public static int crear(Producto pProducto) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
             
                sql = "INSERT INTO Producto(idMarca,Nombre,Descripcion,Precio) VALUES(?,?,?,?)";
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                    ps.setInt(1, pProducto.getIdMarca()); 
                    ps.setString(2, pProducto.getNombre()); 
                    ps.setString(3, pProducto.getDescripcion()); 
                    ps.setString(4, pProducto.getPrecio()); 
                   
                 
                    result = ps.executeUpdate(); 
                    ps.close(); 
                } catch (SQLException ex) {
                    throw ex; 
                }
                conn.close();
            } // Handle any errors that may have occurred.
            catch (SQLException ex) {
                throw ex; 
            }
        return result; 
    }

     // Metodo para poder actualizar un registro en la tabla de Usuario
    public static int modificar(Producto pProducto) throws Exception {
        int result;
        String sql;
        
      
            try (Connection conn = ComunDB.obtenerConexion();) { 
                
                sql = "UPDATE Producto SET IdMarca=?, Nombre=?, Descripcion=?, Precio=? WHERE Id=?";
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                    ps.setInt(1, pProducto.getIdMarca()); 
              
                    ps.setString(2, pProducto.getNombre()); 
                    ps.setString(3, pProducto.getDescripcion()); 
                    ps.setString(4, pProducto.getPrecio()); 
                    ps.setInt(5, pProducto.getId());
                    result = ps.executeUpdate(); 
                    ps.close(); 
                } catch (SQLException ex) {
                    throw ex; 
                }
                conn.close(); 
            } 
            catch (SQLException ex) {
                throw ex; 
            }
        return result;
    }
    

    public static int eliminar(Producto pProducto) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "DELETE FROM Producto WHERE Id=?"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {  
                ps.setInt(1, pProducto.getId()); 
                result = ps.executeUpdate(); 
                ps.close(); //
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex;   
        }
        return result;  
    }


    static int asignarDatosResultSet(Producto pProducto, ResultSet pResultSet, int pIndex) throws Exception {
 
        pIndex++;
        pProducto.setId(pResultSet.getInt(pIndex)); // index 1
        
        pIndex++;
        pProducto.setIdMarca(pResultSet.getInt(pIndex)); // index 2
        
        pIndex++;
        pProducto.setNombre(pResultSet.getString(pIndex)); // index 3
        
        pIndex++;
        pProducto.setDescripcion(pResultSet.getString(pIndex)); // index 4
        
        pIndex++;
        pProducto.setPrecio(pResultSet.getString(pIndex)); // index 5
        
   
        return pIndex;
    
        
    }

    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Producto> pProducto) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { 
            while (resultSet.next()) {
                Producto producto = new Producto();
           
                asignarDatosResultSet(producto, resultSet, 0);
                pProducto.add(producto); 
            }
            resultSet.close(); 
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private static void obtenerDatosIncluirMarca(PreparedStatement pPS, ArrayList<Producto> pProductos) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { 
            HashMap<Integer, Marca> marcaMap = new HashMap();
            while (resultSet.next()) {
                Producto producto = new Producto();
                 
                int index = asignarDatosResultSet(producto, resultSet, 0);
                if (marcaMap.containsKey(producto.getIdMarca()) == false) {                    
                    Marca marca = new Marca();
              
                  MarcaDAL.asignarDatosResultSet(marca, resultSet, index);
                    marcaMap.put(marca.getId(), marca); 
                    producto.setMarca(marca); 
                } else {
                  
                    producto.setMarca(marcaMap.get(producto.getIdMarca())); 
                }
                pProductos.add(producto); 
            }
            resultSet.close(); 
        } catch (SQLException ex) {
            throw ex;         }
    }

    // Metodo para obtener por Id un registro de la tabla de Usuario 
    public static Producto obtenerPorId(Producto pProducto) throws Exception {
        Producto producto = new Producto();
        ArrayList<Producto> productos = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pProducto); 
            sql += " WHERE p.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pProducto.getId()); 
                obtenerDatos(ps, productos); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex; 
        }
        if (productos.size() > 0) { 
            producto= productos.get(0); 
        }
        return producto; 
    }

   
    public static ArrayList<Producto> obtenerTodos() throws Exception {
        ArrayList<Producto> productos;
        productos = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(new Producto()); 
            sql += agregarOrderBy(new Producto()); 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                obtenerDatos(ps, productos); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close();         }
        catch (SQLException ex) {
            throw ex;
        }
        return productos; 
    }


    static void querySelect(Producto pProducto, ComunDB.utilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (pProducto.getId() > 0) {
            pUtilQuery.AgregarNumWhere(" p.Id=? ");
            if (statement != null) {
                statement.setInt(pUtilQuery.getNumWhere(), pProducto.getId());
            }
        }

        if (pProducto.getIdMarca() > 0) {
            pUtilQuery.AgregarNumWhere(" p.IdMarca=? ");
            if (statement != null) {
                statement.setInt(pUtilQuery.getNumWhere(), pProducto.getIdMarca());
            }
        }
        
        if (pProducto.getNombre() != null && pProducto.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" p.Nombre LIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pProducto.getNombre() + "%");
            }
        }

        if (pProducto.getDescripcion() != null && pProducto.getDescripcion().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" p.Descripcion LIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pProducto.getDescripcion() + "%");
            }
        }

        if (pProducto.getPrecio() != null && pProducto.getPrecio().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" p.Precio=? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), pProducto.getPrecio());
            }
        }
    
        
    }

    
    public static ArrayList<Producto> buscar(Producto pProducto) throws Exception {
        ArrayList<Producto> productos = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(pProducto);
            ComunDB comundb = new ComunDB();
            ComunDB.utilQuery utilQuery = comundb.new utilQuery(sql, null, 0); 
            querySelect(pProducto, utilQuery);
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pProducto);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pProducto, utilQuery);
                obtenerDatos(ps, productos);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        return productos;
    }

 
   public static ArrayList<Producto> buscarIncluirMarca(Producto pProducto) throws Exception {
        ArrayList<Producto> productos = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = "SELECT ";
            if (pProducto.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
                sql += "TOP " + pProducto.getTop_aux() + " "; 
            }
            sql += obtenerCampos();
            sql += ",";
            sql += RolDAL.obtenerCampos();
            sql += " FROM Producto p";
            sql += " JOIN Marca r on (p.IdMarca=r.Id)";
            ComunDB comundb = new ComunDB();
            ComunDB.utilQuery utilQuery = comundb.new utilQuery(sql, null, 0);
            querySelect(pProducto, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pProducto);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pProducto, utilQuery);
                obtenerDatosIncluirMarca(ps, productos);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return productos;
    }

}


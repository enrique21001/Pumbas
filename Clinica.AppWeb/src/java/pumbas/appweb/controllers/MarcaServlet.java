/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pumbas.appweb.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



import java.util.ArrayList;
import pumba.accesoadatos.MarcaDAL;
import pumbas.entidadesdenegocio.Marca;
import pumbas.appweb.utils.*;

@WebServlet(name = "MarcaServlet", urlPatterns = {"/Marca"})
public class MarcaServlet extends HttpServlet {
    // <editor-fold defaultstate="collapsed" desc="Métodos para procesar las solicitudes get o post del Servlet">
    
    private Marca obtenerMarca(HttpServletRequest request) {
        String accion = Utilidad.getParameter(request, "accion", "index");
       Marca marca = new Marca();
        if (accion.equals("create") == false) {
            marca.setId(Integer.parseInt(Utilidad.getParameter(request, "id", "0")));
        }

        marca.setNombre(Utilidad.getParameter(request, "nombre", ""));
        if (accion.equals("index")) {
            marca.setTop_aux(Integer.parseInt(Utilidad.getParameter(request, "top_aux", "10")));
            marca.setTop_aux(marca.getTop_aux() == 0 ? Integer.MAX_VALUE : marca.getTop_aux());
        }
        
        return marca;
    }
    
    private void doGetRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Marca marca = new Marca();
            marca.setTop_aux(10);
            ArrayList<Marca> marcas = MarcaDAL.buscar(marca);
            request.setAttribute("marcas", marcas);
            request.setAttribute("top_aux", marca.getTop_aux());             
            request.getRequestDispatcher("Views/Marca/index.jsp").forward(request, response);
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void doPostRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Marca marca = obtenerMarca(request);
            ArrayList<Marca> marcas = MarcaDAL.buscar(marca);
            request.setAttribute("marcas", marcas);
            request.setAttribute("top_aux", marca.getTop_aux());
            request.getRequestDispatcher("Views/Marca/index.jsp").forward(request, response);
        } catch (Exception ex) { 
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void doGetRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("Views/Marca/create.jsp").forward(request, response);
    }
    
    private void doPostRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Marca marca = obtenerMarca(request);
            int result = MarcaDAL.crear(marca);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro registrar un nuevo registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void requestObtenerPorId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Marca marca = obtenerMarca(request);
            Marca marca_result = MarcaDAL.obtenerPorId(marca);
            if (marca_result.getId() > 0) {
                request.setAttribute("marca", marca_result);
            } else {
                Utilidad.enviarError("El Id:" + marca.getId() + " no existe en la tabla de marca", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void doGetRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Marca/edit.jsp").forward(request, response);
    }
    
    private void doPostRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Marca marca = obtenerMarca(request);
            int result = MarcaDAL.modificar(marca);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro actualizar el registro", request, response);
            }
        } catch (Exception ex) {
            // Enviar al jsp de error si hay un Exception
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void doGetRequestDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Marca/details.jsp").forward(request, response);
    }
    
    private void doGetRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Marca/delete.jsp").forward(request, response);
    }
    
    private void doPostRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Marca marca = obtenerMarca(request);
            int result = MarcaDAL.eliminar(marca);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro eliminar el registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos para procesar las peticiones Get y Post">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, "accion", "index");
            switch (accion) {
                case "index":
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doGetRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doGetRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doGetRequestDelete(request, response);
                    break;
                case "details":
                    request.setAttribute("accion", accion);
                    doGetRequestDetails(request, response);
                    break;
                default:
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
            }
        });
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, "accion", "index");
            switch (accion) {
                case "index":
                    request.setAttribute("accion", accion);
                    doPostRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doPostRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doPostRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doPostRequestDelete(request, response);
                    break;
                default:
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
            }
        });
    }
    //</editor-fold>
}


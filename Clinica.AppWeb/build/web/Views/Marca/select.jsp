<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="pumbas.entidadesdenegocio.Marca"%>
<%@page import="pumba.accesoadatos.MarcaDAL"%>
<%@page import="java.util.ArrayList"%>
<% ArrayList<Marca> marcas = MarcaDAL.obtenerTodos();
    int id = Integer.parseInt(request.getParameter("id"));
%>
<select id="slMarca" name="idMarca">
    <option <%=(id == 0) ? "selected" : ""%>  value="0">SELECCIONAR</option>
    <% for (Marca marca : marcas) {%>
    <option <%=(id == marca.getId()) ? "selected" : ""%>  value="<%=marca.getId()%>"><%= marca.getNombre()%></option>
    <%}%>
</select>
<label for="idMarca">Marca</label>


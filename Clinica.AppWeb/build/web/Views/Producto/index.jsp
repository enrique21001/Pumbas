<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="pumbas.entidadesdenegocio.Producto"%>
<%@page import="pumbas.entidadesdenegocio.Marca"%>
<%@page import="java.util.ArrayList"%>
<% ArrayList<Producto> productos = (ArrayList<Producto>) request.getAttribute("productos");
    int numPage = 1;
    int numReg = 10;
    int countReg = 0;
    if (productos == null) {
        productos = new ArrayList();
    } else if (productos.size() > numReg) {
        double divNumPage = (double) productos.size() / (double) numReg;
        numPage = (int) Math.ceil(divNumPage);
    }
    String strTop_aux = request.getParameter("top_aux");
    int top_aux = 10;
    if (strTop_aux != null && strTop_aux.trim().length() > 0) {
        top_aux = Integer.parseInt(strTop_aux);
    }
%>
<!DOCTYPE html>
<html>
    <head>        
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Buscar Productos</title>

    </head>
    
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
           
            <h5>Buscar Productos</h5>
            <form action="Producto" method="post">
                <input type="hidden" name="accion" value="<%=request.getAttribute("accion")%>"> 
               
                
                <div class="row">
                    <div class="input-field col l4 s12">
                        <input  id="txtNombre" type="text" name="nombre">
                        <label for="txtNombre">Nombre</label>
                    </div>  
                 
                    <div class="input-field col l4 s12">
                        <input  id="txtDescripcion" type="text" name="descripcion">
                        <label for="txtDescripcion">Descripcion</label>
                    </div>                   
                     <div class="input-field col l4 s12">
                        <input  id="txtPrecio" type="text" name="precio">
                        <label for="txtPrecio">Precio</label>
                    </div>                   
                    
                                        <div class="input-field col l4 s12">
                        <input  id="txtMarca" type="text" name="idmarca">
                        <label for="txtMarca">Marca</label>
                    </div>
                    <div class="input-field col l4 s12">   
                        <jsp:include page="/Views/Marca/select.jsp">                           
                            <jsp:param name="id" value="0" />  
                        </jsp:include>                        
                    </div>
                    <div class="input-field col l4 s12">   
                        <jsp:include page="/Views/Shared/selectTop.jsp">
                            <jsp:param name="top_aux" value="<%=top_aux%>" />                        
                        </jsp:include>                        
                    </div> 
                </div>
                
                
                <div class="row">
                    <div class="col l12 s12">
                        <button type="submit" class="waves-effect waves-light btn blue"><i class="material-icons right">search</i>Buscar</button>
                        <a href="Producto?accion=create" class="waves-effect waves-light btn blue"><i class="material-icons right">add</i>Crear</a>                          
                    </div>
                </div>
            </form>

            <div class="row">
                <div class="col l12 s12">
                    <div style="overflow: auto">
                        <table class="paginationjs">
                            <thead>
                                <tr> 
      <th>Nombre</th>  
                                    <th>Descripcion</th> 
                                    <th>Precio</th>  
                                  <th>Marca</th> 
                                    <th>Acciones</th>
                                </tr>
                            </thead>                       
                            <tbody>                           
                                <% for (Producto producto : productos) {
                                        int tempNumPage = numPage;
                                        if (numPage > 1) {
                                            countReg++;
                                            double divTempNumPage = (double) countReg / (double) numReg;
                                            tempNumPage = (int) Math.ceil(divTempNumPage);
                                        }
                                %>
                                <tr data-page="<%= tempNumPage%>">  
                                         <td><%=producto.getNombre()%></td>
                                             <td><%=producto.getDescripcion()%></td>
                                                 <td><%=producto.getPrecio()%></td>
                                                                                           
                                                 <td><%=producto.getMarca().getNombre()%></td> 
                                                     
                                 
                                    <td>
                                        <div style="display:flex">
                                             <a href="Producto?accion=edit&id=<%=producto.getId()%>" title="Modificar" class="waves-effect waves-light btn green">
                                            <i class="material-icons">edit</i>
                                        </a>
                                        <a href="Producto?accion=details&id=<%=producto.getId()%>" title="Ver" class="waves-effect waves-light btn blue">
                                            <i class="material-icons">description</i>
                                        </a>
                                        <a href="Producto?accion=delete&id=<%=producto.getId()%>" title="Eliminar" class="waves-effect waves-light btn red">
                                            <i class="material-icons">delete</i>
                                        </a>    
                                        </div>                                                                    
                                    </td>                                   
                                </tr>
                                <%}%>                                                       
                            </tbody>
                        </table>
                    </div>                  
                </div>
            </div>             
            <div class="row">
                <div class="col l12 s12">
                    <jsp:include page="/Views/Shared/paginacion.jsp">
                        <jsp:param name="numPage" value="<%= numPage%>" />                        
                    </jsp:include>
                </div>
            </div>
        
        <jsp:include page="/Views/Shared/footerBody.jsp" />      
    
</html>
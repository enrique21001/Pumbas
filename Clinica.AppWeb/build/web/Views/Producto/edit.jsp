<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="pumbas.entidadesdenegocio.Producto"%>
<% Producto producto = (Producto) request.getAttribute("producto");%>
<!DOCTYPE html>
<html>
    <head>        
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Editar Producto</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
        <main class="container">   
            <h5>Editar Producto</h5>
            <form action="Producto" method="post" onsubmit="return  validarFormulario()">
                <input type="hidden" name="accion" value="<%=request.getAttribute("accion")%>"> 
                <input type="hidden" name="id" value="<%=producto.getId()%>">  
                <div class="row">
                    <div class="input-field col l4 s12">
                        <input  id="txtNombre" type="text" name="nombre" value="<%=producto.getNombre()%>" required class="validate" maxlength="30">
                        <label for="txtNombre">Nombre</label>
                    </div>                       
                    <div class="input-field col l4 s12">
                        <input  id="txtDescripcion" type="text" name="descripcion" value="<%=producto.getDescripcion()%>" required class="validate" maxlength="30">
                        <label for="txtDescripcion">Descripcion</label>
                    </div> 
                    <div class="input-field col l4 s12">
                        <input  id="txtPrecio" type="text" name="precio" value="<%=producto.getPrecio()%>" required  class="validate" maxlength="25">
                        <label for="txtPrecio">Precio</label>
                    </div>                     
                
                    <div class="input-field col l4 s12">   
                        <jsp:include page="/Views/Marca/select.jsp">                           
                            <jsp:param name="id" value="<%=producto.getIdMarca() %>" />  
                        </jsp:include>  
                        <span id="slRol_error" style="color:red" class="helper-text"></span>
                    </div>
                </div>

                <div class="row">
                    <div class="col l12 s12">
                        <button type="sutmit" class="waves-effect waves-light btn blue"><i class="material-icons right">save</i>Guardar</button>
                        <a href="Producto" class="waves-effect waves-light btn blue"><i class="material-icons right">list</i>Cancelar</a>                          
                    </div>
                </div>
            </form>          
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />   
        <script>
            function validarFormulario() {
                var result = true;
                  var slMarca = document.getElementById("slMarca");
                var slMarca_error = document.getElementById("slMarca_error");
               
                if (slMarca.value == 0) {
                    slMarca_error.innerHTML = "La Marca del calzado es obligatorio";
                    result = false;
                } else {
                    slMarca_error.innerHTML = "";
                }

                return result;
            }
        </script>
    </body>
</html>

package pumbas.entidadesdenegocio;



public class Producto {
    private int Id;
    private int IdMarca;
    private String Nombre;
   
    private String Descripcion;
    private String Precio;
        private int top_aux;
        private Marca marca;

    public Producto() {
    }

    public Producto(int Id, int IdMarca, String Nombre, String Descripcion, String Precio) {
        this.Id = Id;
        this.IdMarca = IdMarca;
        this.Nombre = Nombre;
        this.Descripcion = Descripcion;
        this.Precio = Precio;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdMarca() {
        return IdMarca;
    }

    public void setIdMarca(int IdMarca) {
        this.IdMarca = IdMarca;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String Precio) {
        this.Precio = Precio;
    }

    public int getTop_aux() {
        return top_aux;
    }

    public void setTop_aux(int top_aux) {
        this.top_aux = top_aux;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

        
}


    
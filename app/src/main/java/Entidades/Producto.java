package Entidades;

public class Producto {

    private int Id_Empresa, Id_Producto, Id_Estado, Id_Medida, Categoria, Unidades;
    private String Codigo_Producto, Nombre_Producto;
    private double Precio_Unitario, Precio_Total;

    public Producto(int id_Empresa, int id_Producto, int id_Estado, int id_Medida, int categoria, int unidades, String codigo_Producto, String nombre_Producto, double precio_Unitario, double precio_Total) {
        Id_Empresa = id_Empresa;
        Id_Producto = id_Producto;
        Id_Estado = id_Estado;
        Id_Medida = id_Medida;
        Categoria = categoria;
        Unidades = unidades;
        Codigo_Producto = codigo_Producto;
        Nombre_Producto = nombre_Producto;
        Precio_Unitario = precio_Unitario;
        Precio_Total = precio_Total;
    }

    public Producto() {}

    public int getId_Empresa() {
        return Id_Empresa;
    }

    public void setId_Empresa(int id_Empresa) {
        Id_Empresa = id_Empresa;
    }

    public int getId_Producto() {
        return Id_Producto;
    }

    public void setId_Producto(int id_Producto) {
        Id_Producto = id_Producto;
    }

    public int getId_Estado() {
        return Id_Estado;
    }

    public void setId_Estado(int id_Estado) {
        Id_Estado = id_Estado;
    }

    public int getId_Medida() {
        return Id_Medida;
    }

    public void setId_Medida(int id_Medida) {
        Id_Medida = id_Medida;
    }

    public int getCategoria() {
        return Categoria;
    }

    public void setCategoria(int categoria) {
        Categoria = categoria;
    }

    public int getUnidades() {
        return Unidades;
    }

    public void setUnidades(int unidades) {
        Unidades = unidades;
    }

    public String getCodigo_Producto() {
        return Codigo_Producto;
    }

    public void setCodigo_Producto(String codigo_Producto) {
        Codigo_Producto = codigo_Producto;
    }

    public String getNombre_Producto() {
        return Nombre_Producto;
    }

    public void setNombre_Producto(String nombre_Producto) {
        Nombre_Producto = nombre_Producto;
    }

    public double getPrecio_Unitario() {
        return Precio_Unitario;
    }

    public void setPrecio_Unitario(double precio_Unitario) {
        Precio_Unitario = precio_Unitario;
    }

    public double getPrecio_Total() {
        return Precio_Total;
    }

    public void setPrecio_Total(double precio_Total) {
        Precio_Total = precio_Total;
    }
}

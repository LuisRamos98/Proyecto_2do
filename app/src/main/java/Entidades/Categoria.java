package Entidades;

//entidad categoria

public class Categoria {

    private int Id_Empresa, Id_Categoria;
    private String Nombre_Categoria;

    public Categoria(int id_Empresa, int id_Categoria, String nombre_Categoria) {
        Id_Empresa = id_Empresa;
        Id_Categoria = id_Categoria;
        Nombre_Categoria = nombre_Categoria;
    }

    public Categoria() {}

    public int getId_Empresa() {
        return Id_Empresa;
    }

    public void setId_Empresa(int id_Empresa) {
        Id_Empresa = id_Empresa;
    }

    public int getId_Categoria() {
        return Id_Categoria;
    }

    public void setId_Categoria(int id_Categoria) {
        Id_Categoria = id_Categoria;
    }

    public String getNombre_Categoria() {
        return Nombre_Categoria;
    }

    public void setNombre_Categoria(String nombre_Categoria) {
        Nombre_Categoria = nombre_Categoria;
    }
}

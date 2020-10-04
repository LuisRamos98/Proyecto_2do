package Entidades;

public class Rol {
    private int Id_Empresa;
    private int Id_Rol;
    private String DescripcionRol;

    public Rol(int id_Empresa, int id_Rol, String descripcion_Rol) {
        Id_Empresa = id_Empresa;
        Id_Rol = id_Rol;
        DescripcionRol = descripcion_Rol;
    }

    public Rol() {
    }

    public int getId_Empresa() {
        return Id_Empresa;
    }

    public void setId_Empresa(int id_Empresa) {
        Id_Empresa = id_Empresa;
    }

    public int getId_Rol() {
        return Id_Rol;
    }

    public void setId_Rol(int id_Rol) {
        Id_Rol = id_Rol;
    }

    public String getDescripcion_Rol() {
        return DescripcionRol;
    }

    public void setDescripcion_Rol(String descripcion_Rol) {
        DescripcionRol = descripcion_Rol;
    }
}

package Entidades;

public class Usuario {
    private int Id_Empresa;
    private int Id_Usuario;
    private int Estado;
    private String CI;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private int Id_Rol;

    public Usuario(int id_Empresa, int id_Usuario, int estado, String CI,
                   String nombre, String apellido, String correo, int id_Rol) {
        Id_Empresa = id_Empresa;
        Id_Usuario = id_Usuario;
        Estado = estado;
        this.CI = CI;
        Nombre = nombre;
        Apellido = apellido;
        Correo = correo;
        Id_Rol = id_Rol;
    }

    public Usuario() {
    }

    public int getId_Empresa() {
        return Id_Empresa;
    }

    public void setId_Empresa(int id_Empresa) {
        Id_Empresa = id_Empresa;
    }

    public int getId_Usuario() {
        return Id_Usuario;
    }

    public void setId_Usuario(int id_Usuario) {
        Id_Usuario = id_Usuario;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public String getCI() {
        return CI;
    }

    public void setCI(String CI) {
        this.CI = CI;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public int getId_Rol() {
        return Id_Rol;
    }

    public void setId_Rol(int id_Rol) {
        Id_Rol = id_Rol;
    }
}

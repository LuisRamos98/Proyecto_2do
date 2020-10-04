package Entidades;

public class Medida {
    private int Id_Empresa;
    private int Id_Medida;
    private String DescripcionMedida;

    public Medida(int id_Empresa, int id_Medida, String descripcionMedida) {
        Id_Empresa = id_Empresa;
        Id_Medida = id_Medida;
        DescripcionMedida = descripcionMedida;
    }

    public Medida() {
    }

    public int getId_Empresa() {
        return Id_Empresa;
    }

    public void setId_Empresa(int id_Empresa) {
        Id_Empresa = id_Empresa;
    }

    public int getId_Medida() {
        return Id_Medida;
    }

    public void setId_Medida(int id_Medida) {
        Id_Medida = id_Medida;
    }

    public String getDescripcionMedida() {
        return DescripcionMedida;
    }

    public void setDescripcionMedida(String descripcionMedida) {
        DescripcionMedida = descripcionMedida;
    }
}

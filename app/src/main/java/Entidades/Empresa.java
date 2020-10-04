package Entidades;

import com.example.proyecto_2do.MainActivity;

public class Empresa {
    private int IdEmpresa;
    private String RucEmpresa;
    private String NombreEmpresa;

    public Empresa(int idEmpresa, String rucEmpresa, String nombre) {
        this.IdEmpresa = idEmpresa;
        this.RucEmpresa = rucEmpresa;
        this.NombreEmpresa = nombre;
    }

    public Empresa() {}

    public int getIdEmpresa() {
        return IdEmpresa;
    }

    public String getRucEmpresa() {
        return RucEmpresa;
    }

    public String getNombreEmpresa() {
        return NombreEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        IdEmpresa = idEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        RucEmpresa = rucEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        NombreEmpresa = nombreEmpresa;
    }
}

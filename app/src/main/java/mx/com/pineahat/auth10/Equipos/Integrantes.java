package mx.com.pineahat.auth10.Equipos;

/**
 * Created by Ignacio on 17/09/2015.
 */
public class Integrantes {
    private String idAlumno;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String matricula;
    private String estado;

    public Integrantes(String idAlumno, String nombre, String apellidoP, String apellidoM, String matricula, String estado) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.matricula = matricula;
        this.estado = estado;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

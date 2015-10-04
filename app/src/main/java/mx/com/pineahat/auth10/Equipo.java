package mx.com.pineahat.auth10;

/**
 * Created by Stephani on 03/10/2015.
 */
public class Equipo {
    private String idEquiposActividades;
    private String idActividades;
    private String nombre;
    private String fechaModi;
    private String estado;

    public String getIdEquipoTI() {
        return idEquipoTI;
    }

    public void setIdEquipoTI(String idEquipoTI) {
        this.idEquipoTI = idEquipoTI;
    }

    private String idEquipoTI;
    public String getIdEquiposActividades() {
        return idEquiposActividades;
    }

    public void setIdEquiposActividades(String idEquiposActividades) {
        this.idEquiposActividades = idEquiposActividades;
    }

    public String getIdActividades() {
        return idActividades;
    }

    public void setIdActividades(String idActividades) {
        this.idActividades = idActividades;
    }

    public String getFechaModi() {
        return fechaModi;
    }

    public void setFechaModi(String fechaModi) {
        this.fechaModi = fechaModi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}

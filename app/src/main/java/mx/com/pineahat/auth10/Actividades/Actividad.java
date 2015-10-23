package mx.com.pineahat.auth10.Actividades;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephani on 21/10/2015.
 */
public class Actividad {
    private String idAsignacion;
    private String actividadInformacion;
    private String idActividad;
    private String titulo="";
    private String descripcion="";
    private String color ="#e0e0e0";
    private int year=0;
    private int monthOfYear=0;
    private int dayOfMonth=0;
    private String fecha;
    private String hora;
    private int hourOfDay=0;
    private int minute=0;
    private String tipo;
    private String estado;
    private String fechaRealizacion;
    private String fechaCreacion;
    private String fechaActualizacion;

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(String fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }




    private boolean changed=false;



    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        changed=true;
        this.minute = minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        changed=true;
        this.hourOfDay = hourOfDay;
    }


    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        changed=true;
        this.monthOfYear = monthOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        changed=true;
        this.year = year;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        changed=true;
        this.dayOfMonth = dayOfMonth;
    }



    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActiviad) {
        this.idActividad = idActiviad;
    }

    public String getActividadInformacion() {
        return actividadInformacion;
    }

    public void setActividadInformacion(String actividadInformacion) {
        if( actividadInformacion!=null) {
            try {
                JSONObject miJsonObject = new JSONObject(actividadInformacion);
                this.titulo = miJsonObject.getString("nombre");
                this.idActividad = miJsonObject.getString("idActividades");
                this.color = miJsonObject.getString("color");
                this.estado = miJsonObject.getString("estado");
                this.tipo = miJsonObject.getString("tipo");
                this.descripcion = miJsonObject.getString("descripcion");
                this.fechaRealizacion = miJsonObject.getString("fechaRealizacion");
                this.fechaCreacion = miJsonObject.getString("fechaCreacion");
                this.idAsignacion = miJsonObject.getString("idAsignacion");
                this.fechaActualizacion = miJsonObject.getString("fechaActualizacion");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.actividadInformacion = actividadInformacion;
    }


    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }



    public String getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(String idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        changed=true;
        this.color = color;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {

        changed=true;
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        changed=true;
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }





}

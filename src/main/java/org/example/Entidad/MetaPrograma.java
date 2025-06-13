package org.example.Entidad;


public class MetaPrograma {
    private int idMetaPrograma;
    private int idPrograma;
    private int anio;
    private int metaAnual;

    // Constructor vacío
    public MetaPrograma() {}

    // Constructor con todos los campos (excepto idMetaPrograma si es autoincrement)
    public MetaPrograma(int idPrograma, int anio, int metaAnual) {
        this.idPrograma = idPrograma;
        this.anio = anio;
        this.metaAnual = metaAnual;
    }

    // Getters y setters
    public int getIdMetaPrograma() {
        return idMetaPrograma;
    }

    public void setIdMetaPrograma(int idMetaPrograma) {
        this.idMetaPrograma = idMetaPrograma;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMetaAnual() {
        return metaAnual;
    }

    public void setMetaAnual(int metaAnual) {
        this.metaAnual = metaAnual;
    }

    // Puedes agregar método toString para debug o mostrar info
    @Override
    public String toString() {
        return "MetaPrograma{" +
                "idMetaPrograma=" + idMetaPrograma +
                ", idPrograma=" + idPrograma +
                ", anio=" + anio +
                ", metaAnual=" + metaAnual +
                '}';
    }
}

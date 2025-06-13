package org.example.Entidad;

public class MetaObstetra {
    private int idMetaObstetra;
    private int idObstetra;
    private int idPrograma;
    private int anio;
    private int metaAnual;
    private int metaMensual;

    public MetaObstetra() {
    }

    public MetaObstetra(int idMetaObstetra, int idObstetra, int idPrograma, int anio, int metaAnual, int metaMensual) {
        this.idMetaObstetra = idMetaObstetra;
        this.idObstetra = idObstetra;
        this.idPrograma = idPrograma;
        this.anio = anio;
        this.metaAnual = metaAnual;
        this.metaMensual = metaMensual;
    }

    // Getters y Setters

    public int getIdMetaObstetra() {
        return idMetaObstetra;
    }

    public void setIdMetaObstetra(int idMetaObstetra) {
        this.idMetaObstetra = idMetaObstetra;
    }

    public int getIdObstetra() {
        return idObstetra;
    }

    public void setIdObstetra(int idObstetra) {
        this.idObstetra = idObstetra;
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

    public int getMetaMensual() {
        return metaMensual;
    }

    public void setMetaMensual(int metaMensual) {
        this.metaMensual = metaMensual;
    }
}

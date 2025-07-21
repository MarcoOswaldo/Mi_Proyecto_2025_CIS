package org.example.Entidades;

public class MetaPrograma {
    private int idMetaPrograma;
    private int idPrograma;
    private int anio;
    private int metaAnual;
    private int metaMensual;

    public MetaPrograma() {
    }

    public MetaPrograma(int idMetaPrograma, int idPrograma, int anio, int metaAnual, int metaMesual) {
        this.idMetaPrograma = idMetaPrograma;
        this.idPrograma = idPrograma;
        this.anio = anio;
        this.metaAnual = metaAnual;
        this.metaMensual = metaMesual;
    }

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

    public int getMetaMensual() {
        return metaMensual;
    }

    public void setMetaMensual(int metaMensual) {
        this.metaMensual = metaMensual;
    }
}

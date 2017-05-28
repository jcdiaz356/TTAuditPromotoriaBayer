package com.dataservicios.ttauditpromotoriabayer.Model;

/**
 * Created by usuario on 10/01/2015.
 */
public class Ruta {

    private String RutaDia;
    private int Pdvs;
    private int PorcentajeAvance;
    private int Id;
    //private ArrayList<String> genre;

    public Ruta() {
    }

    public Ruta(String RutaDia, int Pdvs, int PorcentajeAvance , int Id) {
        this.RutaDia = RutaDia;
        this.Pdvs = Pdvs;
        this.PorcentajeAvance = PorcentajeAvance;
        this.Id = Id;

    }
    public int getId() {
        return Id;
    }
    public void setId(int Id) {
        this.Id = Id;
    }
    public String getRutaDia() {
        return RutaDia;
    }

    public void setRutaDia(String RutaDia) {
        this.RutaDia = RutaDia;
    }


    public int getPdvs() {
        return Pdvs;
    }

    public void setPdvs(int Pdvs) {
        this.Pdvs = Pdvs;
    }

    public double getPorcentajeAvance() {
        return PorcentajeAvance;
    }

    public void setPorcentajeAvance(int PorcentajeAvance) {
        this.PorcentajeAvance = PorcentajeAvance;
    }


}

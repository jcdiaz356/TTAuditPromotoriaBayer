package com.dataservicios.ttauditpromotoriabayer.Model;



/**
 * Created by usuario on 10/01/2015.
 */
public class Pdv {

    private String thumbnailUrl, Pdv, Direccion , Distrito ;
    private int Status, id;
    private String type ;
    private String typeBodega;
    private String region;

    //private ArrayList<String> genre;


    public Pdv() {
    }

    public Pdv(String thumbnailUrl, String Pdv, String Direccion, String Distrito, int Status , int id) {

        this.thumbnailUrl = thumbnailUrl;
        this.Pdv = Pdv;
        this.Direccion = Direccion;
        this.Distrito = Distrito;
        this.Status = Status;
        this.id= id;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPdv() {
        return Pdv;
    }

    public void setPdv(String Pdv) {
        this.Pdv = Pdv;
    }




    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getDistrito() {
        return Distrito;
    }

    public void setDistrito(String Distrito) {
        this.Distrito = Distrito;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getTypeBodega() {
        return typeBodega;
    }

    public void setTypeBodega(String typeBodega) {
        this.typeBodega = typeBodega;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

package com.dataservicios.ttauditpromotoriabayer.Model;

/**
 * Created by Jaime Eduardo on 05/10/2015.
 */
public class Audit {

    private int id;
    private String name;
    private int store_id;
    private int company_id;
    private int route_id;
    private int score;
    private int user_id;

    private String time_open;
    private String time_close;
    private String latitude_open;
    private String latitude_close;
    private String longitude_open;
    private String longitude_close;



    private String created_at;

    /**
     * Return id the audit
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Set id the audit
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get name the audit
     *
     * @return name
     */

    public String getName() {
        return name;
    }

    /**
     * Set name the audit
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get store id the audit
     *
     * @return store_id
     */
    public int getStore_id() {
        return store_id;
    }

    /**
     * Set store id the audit
     *
     * @param store_id
     */
    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }


    /**
     * Get Score the auditory
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set Score the auditory
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }


    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTime_open() {
        return time_open;
    }

    public void setTime_open(String time_open) {
        this.time_open = time_open;
    }

    public String getTime_close() {
        return time_close;
    }

    public void setTime_close(String time_close) {
        this.time_close = time_close;
    }

    public String getLatitude_open() {
        return latitude_open;
    }

    public void setLatitude_open(String latitude_open) {
        this.latitude_open = latitude_open;
    }

    public String getLatitude_close() {
        return latitude_close;
    }

    public void setLatitude_close(String latitude_close) {
        this.latitude_close = latitude_close;
    }

    public String getLongitude_open() {
        return longitude_open;
    }

    public void setLongitude_open(String longitude_open) {
        this.longitude_open = longitude_open;
    }

    public String getLongitude_close() {
        return longitude_close;
    }

    public void setLongitude_close(String longitude_close) {
        this.longitude_close = longitude_close;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

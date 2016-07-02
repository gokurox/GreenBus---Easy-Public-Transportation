package com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder;

/**
 * Created by Gursimran Singh on 30-04-2016.
 */
public class Edges {
    public String vertex_1;
    public String vertex_2;
    public Double weight;

    public Edges(String vertex_1, String vertex_2, Double weight) {
        this.vertex_1 = vertex_1;
        this.vertex_2 = vertex_2;
        this.weight = weight;
    }
}

package com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder;

/**
 * Created by Gursimran Singh on 27-04-2016.
 */
public class Feedback {
    public String bus_number;
    public String feedback;
    public Float rating;
    public String email;

    public Feedback(String bus_number, String feedback, Float rating, String email) {
        this.email = email;
        this.bus_number = bus_number;
        this.feedback = feedback;
        this.rating = rating;
    }
}

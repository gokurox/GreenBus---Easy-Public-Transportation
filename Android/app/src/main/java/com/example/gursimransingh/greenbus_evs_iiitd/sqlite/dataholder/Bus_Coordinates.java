package com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder;

/**
 * Created by Gursimran Singh on 29-04-2016.
 */
public class Bus_Coordinates {
    public String bus_number;
    public Integer stop_number;
    public String stop_name;
    public Double latitude, longitude;

    public Bus_Coordinates(String bus_number, Integer stop_number, String stop_name, Double latitude, Double longitude) {
        this.bus_number = bus_number;
        this.stop_number = stop_number;
        this.stop_name = stop_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

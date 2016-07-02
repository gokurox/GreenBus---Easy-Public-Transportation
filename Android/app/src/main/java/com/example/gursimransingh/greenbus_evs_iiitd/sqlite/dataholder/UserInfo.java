package com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder;

/**
 * Created by Gursimran Singh on 09-04-2016.
 */
public class UserInfo {
    public String email;
    public String mobile;
    public String name;
    public String password;
    public String login_status;

    public UserInfo(String email, String mobile, String name, String password, String login_status) {
        this.email = email;
        this.mobile = mobile;
        this.name = name;
        this.password = password;
        this.login_status = login_status;
    }
}

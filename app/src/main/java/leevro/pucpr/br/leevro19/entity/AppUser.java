package leevro.pucpr.br.leevro19.entity;

import com.google.gson.annotations.SerializedName;
/**
 * Created by Jonas on 22/04/2016.
 */
public class AppUser {

    public String name;

    public String email;

    public String facebookID;

    public String gender;

    private static AppUser ourInstance = new AppUser();

    public static AppUser getInstance() {
        return ourInstance;
    }

    public AppUser() {
    }
}

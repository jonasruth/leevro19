package leevro.pucpr.br.leevro19.entity;

import com.google.gson.annotations.SerializedName;
/**
 * Created by Jonas on 22/04/2016.
 */
public class AppUser {

    public String userId;

    public String name;

    public String firstName;

    public String lastName;

    public String birthday;

    public String email;

    public String facebookID;

    public String gender;

    public String locationId;

    public String locationName;

    public String locale;

    public int timezone;

    public String link;

    public String updatedTime;

    public Boolean verified;

    private static AppUser ourInstance = new AppUser();

    public static AppUser getInstance() {
        return ourInstance;
    }

    public AppUser() {
    }
}

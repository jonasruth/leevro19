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

    // Coisas de Localização

    public Double geoLatitude;
    public Double geoLongitude;

    // Coisas de Facebook

    public String facebookID;

    public String gender;

    public String locationId;

    public String locationName;

    public String locale;

    public String timezone;

    public String link;

    public String updatedTime;

    public String verified;

    private static AppUser ourInstance = new AppUser();

    public static AppUser getInstance() {
        return ourInstance;
    }

    public AppUser() {
    }
}

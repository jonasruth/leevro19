package leevro.pucpr.br.leevro19.entity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jonas on 12/04/2016.
 */
public class Book extends VirtualBook {

    private String physicalBookId;
    private String ownerUserId;
    private Integer requests;
    private String availability;

    private Double lat;
    private Double lng;

    public Book() {
    }

    public String getPhysicalBookId() {
        return physicalBookId;
    }

    public void setPhysicalBookId(String physicalBookId) {
        this.physicalBookId = physicalBookId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Integer getRequests() {
        return requests;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}

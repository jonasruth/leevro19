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
}

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

    public Book(JSONObject jbook) {
        try {
            physicalBookId = jbook.getString("fbook_id");
            virtualBookId = jbook.getString("vbook_id");
            ownerUserId = jbook.getString("user_id");
            isbn = jbook.getString("isbn");
            title = jbook.getString("title");
            photo = jbook.getString("photo");
            authorName = jbook.getString("author_name");
            genderName = jbook.getString("gender_name");
            description = jbook.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

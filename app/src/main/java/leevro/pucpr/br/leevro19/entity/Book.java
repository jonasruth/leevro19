package leevro.pucpr.br.leevro19.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jonas on 12/04/2016.
 */
public class Book {

    private String isbn;

    private String title;

    private String photo;

    private String authorName;

    private String genderName;

    private String description;

    public Book(){}

    public Book(JSONObject jbook){
        try {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

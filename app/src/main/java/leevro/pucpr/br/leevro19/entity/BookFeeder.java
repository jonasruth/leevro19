package leevro.pucpr.br.leevro19.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jonas on 23/04/2016.
 */
public class BookFeeder {

    public static Book bookFromJSONObject(JSONObject jBook) {
        Book book = new Book();

        try {

            book.setLat(jBook.getDouble("geo_last_lat"));
            book.setLng(jBook.getDouble("geo_last_lng"));

            book.setPhysicalBookId(jBook.getString("fbook_id"));
            book.setVirtualBookId(jBook.getString("vbook_id"));
            book.setOwnerUserId(jBook.getString("user_id"));
            book.setIsbn(jBook.getString("isbn"));
            book.setTitle(jBook.getString("title"));
            book.setPhoto(jBook.getString("photo"));
            book.setAuthorName(jBook.getString("author_name"));
            book.setGenderName(jBook.getString("gender_name"));
            book.setDescription(jBook.getString("description"));
            book.setRequests(jBook.getInt("requests"));
            book.setAvailability(jBook.getString("availability"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }

    public static VirtualBook virtualBookFromJSONObject(JSONObject jVBook) {
        VirtualBook book = new VirtualBook();

        try {
            book.setVirtualBookId(jVBook.getString("vbook_id"));
            book.setIsbn(jVBook.getString("isbn"));
            book.setTitle(jVBook.getString("title"));
            book.setPhoto(jVBook.getString("photo"));
            book.setAuthorName(jVBook.getString("author_name"));
            book.setGenderName(jVBook.getString("gender_name"));
            book.setDescription(jVBook.getString("description"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }

    public static BookCollection booksFromJSONArray(JSONArray bookArr) throws JSONException {
        BookCollection books = new BookCollection();

        int size = bookArr.length();
        for (int i = 0; i < size; i++) {
            books.add(bookFromJSONObject(bookArr.getJSONObject(i)));
        }
        return books;
    }

    public static VirtualBookCollection virtualBooksFromJSONArray(JSONArray vbookArr) throws JSONException {
        VirtualBookCollection vbooks = new VirtualBookCollection();

        int size = vbookArr.length();
        for (int i = 0; i < size; i++) {
            vbooks.add(virtualBookFromJSONObject(vbookArr.getJSONObject(i)));
        }
        return vbooks;
    }
}

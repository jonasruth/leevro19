package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import leevro.pucpr.br.leevro19.entity.Book;

public class BookDetailActivity extends ActionBarActivity {

    TextView isbn;
    TextView bookTitle;
    TextView bookAuthorName;
    TextView bookGenderName;
    TextView bookDescription;
    ImageView bookCover;
    String photo;
    Book livro;

    public void goToPublicProfile(View view) {
        Intent intent = new Intent(BookDetailActivity.this, PublicProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String p_isbn = null;
        if (extras != null) {
            p_isbn = extras.getString("isbn");
        }

        // começa a parada
        isbn = (TextView) findViewById(R.id.isbn);
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthorName = (TextView) findViewById(R.id.bookAuthorName);
        bookGenderName = (TextView) findViewById(R.id.bookGenderName);
        bookDescription = (TextView) findViewById(R.id.bookDescription);
        bookCover = (ImageView) findViewById(R.id.bookCover);

        String url = "http://96.126.115.143/leevrows/retornaUmLivro.php?isbn=" + p_isbn;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {
                            livro = new Book(response.getJSONObject("livro"));
                            isbn.setText(livro.getIsbn());
                            bookTitle.setText(livro.getTitle());
                            bookAuthorName.setText(livro.getAuthorName());
                            bookGenderName.setText(livro.getGenderName());
                            bookDescription.setText(livro.getDescription());
//                            bookPhoto.setText(livro.getString("photo"));
                            photo = livro.getPhoto();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new VolleyCallback() {
                            @Override
                            public void onSuccess() {
                                loadCover();
                            }
                        }.onSuccess();
                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro: ", error.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
        Volley.newRequestQueue(this).add(jsObjRequest);
    }

    public interface VolleyCallback {
        void onSuccess();
    }

    private void loadCover() {
        ImageRequest request = new ImageRequest("http://96.126.115.143/leevrows/vbook_img/" + livro.getPhoto(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        bookCover.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //bookCover.setImageResource(R.drawable.image_load_error);
                    }
                });
// Access the RequestQueue through your singleton class.
        Volley.newRequestQueue(this).add(request);
    }

}

package leevro.pucpr.br.leevro19;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BookAddActivity extends ActionBarActivity {

    EditText isbn;
    TextView bookTitle;
    TextView bookAuthorName;
    TextView bookGenderName;
    TextView bookDescription;
    TextView bookYear;
    TextView bookLanguage;
    //    TextView bookPhoto;
    TextView bookEdition;
    ImageView bookCover;
    String photo;
    ScrollView bookDetailPreview;

    JSONObject livro;

    public void goToBookPropose(View view) {

        hideSoftKeyboard(BookAddActivity.this);

        isbn = (EditText) findViewById(R.id.isbn);
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthorName = (TextView) findViewById(R.id.bookAuthorName);
        bookGenderName = (TextView) findViewById(R.id.bookGenderName);
        bookDescription = (TextView) findViewById(R.id.bookDescription);
        bookEdition = (TextView) findViewById(R.id.bookEdition);
        bookCover = (ImageView) findViewById(R.id.bookCover);

        //Toast toast = Toast.makeText(getApplicationContext(), isbn.getText(), Toast.LENGTH_SHORT);
        //toast.show();
        String url = "http://96.126.115.143/leevrows/retornaUmLivro.php?isbn="+isbn.getText();
        Log.d("Retorno: ", "xxadadas");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {
                            livro = response.getJSONObject("livro");
                            bookTitle.setText(livro.getString("title"));
                            bookAuthorName.setText(livro.getString("author_name"));
                            bookGenderName.setText(livro.getString("gender_name"));
                            bookDescription.setText(livro.getString("description"));
//                            bookPhoto.setText(livro.getString("photo"));
                            bookEdition.setText(livro.getString("edition"));
                            bookDetailPreview.setVisibility(ScrollView.VISIBLE);
                            photo = livro.getString("photo");

                            Thread thread = new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    URL url = null;
                                    try {
                                        url = new URL("http://96.126.115.143/leevrows/vbook_img/"+livro.getString("photo"));
                                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        bookCover.setImageBitmap(bmp);

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            thread.start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        //Volley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void addBook(View view) {

        hideSoftKeyboard(BookAddActivity.this);

        isbn = (EditText) findViewById(R.id.isbn);

        String url = "http://96.126.115.143/leevrows/adicionaUmLivro.php";

        Map<String, String> params = new HashMap();
        params.put("isbn", isbn.getText().toString());
        params.put("user_id", "1");
        params.put("photo", photo);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());

                        try {
                            JSONObject status = response.getJSONObject("status");
                            Boolean sucesso = status.getBoolean("success");

                            Intent intent = new Intent(BookAddActivity.this, BookGaleryActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        //Volley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);
        bookDetailPreview = (ScrollView) findViewById(R.id.bookDetailPreview);
        bookDetailPreview.setVisibility(ScrollView.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}

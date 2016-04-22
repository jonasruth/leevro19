package leevro.pucpr.br.leevro19;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void goToBookPropose(View view) {

        hideSoftKeyboard(BookAddActivity.this);

        isbn = (EditText) findViewById(R.id.isbn);
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthorName = (TextView) findViewById(R.id.bookAuthorName);
        bookGenderName = (TextView) findViewById(R.id.bookGenderName);
        bookDescription = (TextView) findViewById(R.id.bookDescription);
        bookEdition = (TextView) findViewById(R.id.bookEdition);
        bookCover = (ImageView) findViewById(R.id.bookCover);

        String url = "http://96.126.115.143/leevrows/retornaUmLivro.php?isbn=" + isbn.getText();
        Log.d("Retorno: ", "xxadadas");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {

                            Boolean status = response.getBoolean("status");
                            if (status) {
                                livro = response.getJSONObject("livro");
                                bookTitle.setText(livro.getString("title"));
                                bookAuthorName.setText(livro.getString("author_name"));
                                bookGenderName.setText(livro.getString("gender_name"));
                                bookDescription.setText(livro.getString("description"));
//                            bookPhoto.setText(livro.getString("photo"));
                                bookEdition.setText(livro.getString("edition"));
                                bookDetailPreview.setVisibility(ScrollView.VISIBLE);
                                photo = livro.getString("photo");

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        URL url = null;
                                        try {
                                            url = new URL("http://96.126.115.143/leevrows/vbook_img/" + livro.getString("photo"));
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
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(), "Erro: Não foi possível retornar livro pesquisado.", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        } catch (
                                JSONException e
                                )

                        {
                            e.printStackTrace();
                        }
                    }
                }

                        , new Response.ErrorListener()

                {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro: ", error.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }

                );
        Volley.newRequestQueue(this).

                add(jsObjRequest);
        //Volley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void addBook(View view) {
        addBook();
    }

    public void addBook() {

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_favorite:
                // About option clicked.
                addBook();
                return true;
//            case R.id.action_settings:
//                // Settings option clicked.
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BookAdd Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://leevro.pucpr.br.leevro19/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BookAdd Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://leevro.pucpr.br.leevro19/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

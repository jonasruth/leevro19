package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.network.CustomVolleyRequestQueue;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class BookAddActivity extends ActionBarActivity {

    TextView find;

    EditText isbn;
    TextView bookTitle;
    TextView bookAuthorName;
    TextView bookGenderName;
    TextView bookDescription;
    TextView bookYear;
    TextView bookLanguage;
    //    TextView bookPhoto;
    TextView bookEdition;
    NetworkImageView bookCover;
    String photo;
    LinearLayout bookDetailPreview;
    LinearLayout consulta;
    JSONObject livro;
    private ImageLoader mImageLoader;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);
        consulta = (LinearLayout) findViewById(R.id.consulta);
        bookDetailPreview = (LinearLayout) findViewById(R.id.bookDetailPreview);
        consulta.setVisibility(LinearLayout.VISIBLE);
        bookDetailPreview.setVisibility(ScrollView.GONE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        isbn = (EditText) findViewById(R.id.isbn);
        find = (TextView) findViewById(R.id.find);

        String barcodeScanISBN = PrefUtils.getCurrentISBN(getApplicationContext());
        if (barcodeScanISBN != null && !barcodeScanISBN.isEmpty()) {
            Log.d("BookAddActivity 1", barcodeScanISBN);
            //goToBookPropose(barcodeScanISBN);
            isbn.setText(barcodeScanISBN);
            goToBookPropose(barcodeScanISBN);
            //find.callOnClick();
            Log.d("BookAddActivity 2", barcodeScanISBN);
            PrefUtils.clearCurrentISBN(getApplicationContext());
        } else {
            Log.d("BookAddActivity 2", "NULO");
        }

    }

    public void lerCodigoDeBarras(View view) {
        Intent intent = new Intent(BookAddActivity.this, BarcodeScannerActivity.class);
        startActivity(intent);
    }

    public void goToBookPropose(View view) {
//        AppUtils.esconderTecladoVirtual(this);
        goToBookPropose(isbn.getText().toString());
    }

    public void goBack(View view) {
        getSupportActionBar().show();
        consulta.setVisibility(LinearLayout.VISIBLE);
        bookDetailPreview.setVisibility(LinearLayout.GONE);
        isbn.setText(null);
    }

    private void goToBookPropose(String isbnToSearch) {
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthorName = (TextView) findViewById(R.id.bookAuthorName);
        bookGenderName = (TextView) findViewById(R.id.bookGenderName);
        bookDescription = (TextView) findViewById(R.id.bookDescription);
        bookEdition = (TextView) findViewById(R.id.bookEdition);
        bookCover = (NetworkImageView) findViewById(R.id.bookCover);

        String url = "http://96.126.115.143/leevrows/retornaUmLivro.php?isbn=" + isbnToSearch;
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
                                bookEdition.setText(livro.getString("edition"));
                                photo = livro.getString("photo");

                                mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getImageLoader();
                                bookCover.setImageUrl(AppUtils.APP_PATH_VIRTUAL_BOOK_COVER_PATH + livro.getString("photo"), mImageLoader);

                                consulta.setVisibility(LinearLayout.GONE);
                                bookDetailPreview.setVisibility(LinearLayout.VISIBLE);

                                getSupportActionBar().hide();

                            } else {
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
        Volley.newRequestQueue(this).add(jsObjRequest);

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                2f));
        //Volley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void addBook(View view) {
//        AppUtils.esconderTecladoVirtual(this);
        addBook();
        /*ImageView spaceshipImage = (ImageView) findViewById(R.id.bookCover);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.test);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        hyperspaceJumpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addBook();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
    }

    public void addBook() {
        String url = "http://96.126.115.143/leevrows/adicionaUmLivro.php";

        Map<String, String> params = new HashMap();
        params.put("isbn", isbn.getText().toString());
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
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

                            Intent intent = new Intent(BookAddActivity.this, BookGalleryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

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


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_book_add, menu);
        return true;
    }*/

   /* @Override
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
    }*/

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

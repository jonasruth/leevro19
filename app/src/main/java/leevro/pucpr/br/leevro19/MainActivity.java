package leevro.pucpr.br.leevro19;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends ActionBarActivity {

    private ImageView bookCover;
//    private TextView bookTitle;
//    private TextView bookAuthor;
    private JSONArray listaLivros;
    private JSONObject livroAtual;
    private int bookListIndex = -1;

    public void loadBookListForChoice() {

        bookListIndex = -1;

        String url = "http://96.126.115.143/leevrows/retornaListaLivrosParaEscolha.php";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {
                            listaLivros = response.getJSONArray("livros");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new VolleyCallback(){
                            @Override
                            public void onSuccess() {
                                nextBook();
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

    public interface VolleyCallback{
        void onSuccess();
    }

    public void gotoNextBook(View view){
        nextBook();
    }

    public void nextBook()
    {

        bookCover = (ImageView) findViewById(R.id.bookCover);
//        bookTitle = (TextView) findViewById(R.id.bookTitle);
//        bookAuthor = (TextView) findViewById(R.id.bookAuthor);

        if(bookListIndex < listaLivros.length()-1) {
            ++bookListIndex;
        }else{
            return;
        }

        try {

            livroAtual = listaLivros.getJSONObject(bookListIndex);

//            bookTitle.setText(livroAtual.getString("title"));
//            bookAuthor.setText(livroAtual.getString("author"));

            // Retrieves an image specified by the URL, displays it in the UI.
            ImageRequest request = new ImageRequest("http://96.126.115.143/leevrows/vbook_img/"+livroAtual.getString("photo"),
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void goToBookAdd(View view)
    {
        Intent intent = new Intent(MainActivity.this, BookAddActivity.class);
        startActivity(intent);
    }

    public void goToBookGalery()
    {
        Intent intent = new Intent(MainActivity.this, BookGaleryActivity.class);
        startActivity(intent);
    }

    public void goToBookDetail(View view)
    {
        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
        try {
            Log.d("Livro atual", livroAtual.getString("isbn"));
            intent.putExtra("isbn", livroAtual.getString("isbn"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBookListForChoice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_my_book_gallery:
                // About option clicked.
                goToBookGalery();
                return true;
            case R.id.action_settings:
                // Settings option clicked.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }
}

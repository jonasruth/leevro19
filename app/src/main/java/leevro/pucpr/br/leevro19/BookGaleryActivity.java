package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leevro.pucpr.br.leevro19.adapter.BookAdapter;
import leevro.pucpr.br.leevro19.entity.Book;

public class BookGaleryActivity extends ActionBarActivity {

    private JSONArray livros;
    ListView listView;



    public void goToBookAdd(View view)
    {
        goToBookAdd();
    }

    private void goToBookAdd()
    {
        Intent intent = new Intent(BookGaleryActivity.this, BookAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_galery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        String url = "http://96.126.115.143/leevrows/retornaListaLivrosParaEscolha.php";
        listView = (ListView) findViewById(R.id.listView);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());
                        try {
                            livros = response.getJSONArray("livros");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new VolleyCallback(){
                            @Override
                            public void onSuccess() {
                                createListView();
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

    private void createListView(){
        listView.setAdapter(new BookAdapter(this, livros));
    }

    public interface VolleyCallback{
        void onSuccess();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_galery, menu);
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
            case R.id.action_book_add:
                // About option clicked.
                goToBookAdd();
                return true;
//            case R.id.action_settings:
//                // Settings option clicked.
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

}

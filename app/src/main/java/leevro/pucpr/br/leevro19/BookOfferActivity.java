package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.adapter.BookSelectAdapter;
import leevro.pucpr.br.leevro19.entity.BookCollection;
import leevro.pucpr.br.leevro19.entity.BookFeeder;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class BookOfferActivity extends Activity {

    private BookCollection livros;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_offer);

        Map<String, String> params = new HashMap();
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        JSONObject parameters = new JSONObject(params);

        listView = (ListView) findViewById(R.id.listView);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_GET_MY_BOOKS, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());
                        try {
                            livros = BookFeeder.booksFromJSONArray(response.getJSONArray("livros"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new VolleyCallback() {
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

    public interface VolleyCallback {
        void onSuccess();
    }

    private void createListView() {

        listView.setAdapter(new BookSelectAdapter(this, livros));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                (findViewById(R.id.chk)).setSelected(false);

//                listView.getAdapter().getView(position)
//
//                CheckBox chk = (CheckBox) view.findViewById(R.id.chk);
//                chk.setSelected(true);
                Toast.makeText(getApplicationContext(),"Checkbox: "+(findViewById(R.id.chk)).isSelected(),Toast.LENGTH_SHORT).show();
//                String fbook_id = null;
//                fbook_id = livros.get(position).getPhysicalBookId();
//                goToBookDetail(view, fbook_id);
            }
        });


    }

    public void confirm(View view) {
        Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);
//        intent.putExtra("p_fbook_id", fbook_id);
        startActivity(intent);
    }

    public void selectAllBooks(View view){
        for ( int i=0; i < listView.getAdapter().getCount(); i++) {
            listView.setItemChecked(i, true);
        }
    }

}

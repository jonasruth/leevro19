package leevro.pucpr.br.leevro19;

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

import leevro.pucpr.br.leevro19.adapter.TradeAdapter;
import leevro.pucpr.br.leevro19.entity.Trade;
import leevro.pucpr.br.leevro19.entity.TradeCollection;
import leevro.pucpr.br.leevro19.entity.TradeFeeder;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class BookTransactionActivity extends Activity {

    private TradeCollection transactions;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_transaction);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Map<String, String> params = new HashMap();
        params.put("user_id", PrefUtils.getLoggedUser(this.getApplicationContext()).userId);
        JSONObject parameters = new JSONObject(params);

        listView = (ListView) findViewById(R.id.listView2);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_GET_MY_TRANSACTIONS, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());
                        try {
                            transactions = TradeFeeder.tradesFromJSONArray(response.getJSONArray("trades"));
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
//                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
//                        toast.show();

                    }
                });
        Volley.newRequestQueue(this).add(jsObjRequest);
    }

    public interface VolleyCallback {
        void onSuccess();
    }

    private void createListView() {

        listView.setAdapter(new TradeAdapter(this, transactions));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToTradeDetail(view, transactions.get(position));
            }
        });

    }

    public void goToTradeDetail(View view, Trade trade) {
//        Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
////        intent.putExtra("p_fbook_id", fbook_id);
//        PrefUtils.setCurrentBook(book,getActivity().getApplicationContext());
//        startActivity(intent);
    }

}

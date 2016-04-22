package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.adapter.BookAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookGaleryFragment extends Fragment {

    private JSONArray livros;
    ListView listView;

    String p_fbook_id = null;
    String p_user_id = null;

    public BookGaleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_galery, container, false);
        // inicio meu codigo

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            p_fbook_id = extras.getString("p_fbook_id");
            p_user_id = extras.getString("p_user_id");
            Log.d("Extras >>> ", extras.toString());
        }

        if(p_user_id==null){
            p_user_id = "1"; // usuario utilizando
        }

        Log.d("BookGaleryFragment >>> ", p_user_id);

        String url = "http://96.126.115.143/leevrows/retornaMeusLivros.php";

        Map<String, String> params = new HashMap();
        params.put("user_id", p_user_id);
        JSONObject parameters = new JSONObject(params);

        listView = (ListView) view.findViewById(R.id.listView);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());
                        try {
                            livros = response.getJSONArray("livros");
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
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
        Volley.newRequestQueue(getActivity()).add(jsObjRequest);

        //fim meu codigo

        return view;
    }

    public interface VolleyCallback {
        void onSuccess();
    }

    private void createListView() {

        listView.setAdapter(new BookAdapter(getActivity(), livros));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity().getApplicationContext(),"clicou",Toast.LENGTH_SHORT).show();
                String fbook_id = null;
                try {
                    fbook_id = livros.getJSONObject(position).getString("fbook_id");
                    //user_id = livros.getJSONObject(position).getString("fbook_id");
                    goToBookDetail(view, fbook_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void goToBookDetail(View view, String fbook_id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);

        intent.putExtra("p_fbook_id", fbook_id);
        intent.putExtra("p_user_id", fbook_id);
        startActivity(intent);
    }

}

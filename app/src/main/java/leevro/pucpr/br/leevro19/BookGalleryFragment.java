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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.adapter.BookAdapter;
import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.entity.Book;
import leevro.pucpr.br.leevro19.entity.BookCollection;
import leevro.pucpr.br.leevro19.entity.BookFeeder;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookGalleryFragment extends Fragment {

    //private JSONArray livros;
    private BookCollection livros;

    ListView listView;

    private AppUser targetUser;
    private AppUser loggedUser;

//    String p_fbook_id = null;
//    String p_user_id = null;

    public BookGalleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_galery, container, false);
        // inicio meu codigo

        Map<String, String> params = new HashMap();
        params.put("user_id", PrefUtils.getLoggedUser(getActivity().getApplicationContext()).userId);
        JSONObject parameters = new JSONObject(params);

        listView = (ListView) view.findViewById(R.id.listView);

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
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
        Volley.newRequestQueue(getActivity()).add(jsObjRequest);

        targetUser = PrefUtils.getTargetUser(getActivity().getApplicationContext());
        loggedUser = PrefUtils.getLoggedUser(getActivity().getApplicationContext());

        Log.d("XXXX", targetUser.userId);
        Log.d("YYYY", PrefUtils.getLoggedUser(getActivity().getApplicationContext()).userId);
        if (targetUser == null || targetUser.userId.equals(PrefUtils.getLoggedUser(getActivity().getApplicationContext()).userId)) {
            view.findViewById(R.id.btnAdicionarLivro).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.btnAdicionarLivro).setVisibility(View.GONE);
        }


        TextView button = (TextView) view.findViewById(R.id.btnAdicionarLivro);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(),"Adição de Livros",Toast.LENGTH_SHORT).show();
                goToBookAdd();
            }
        });

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
                String fbook_id = null;
//                livros.get(position).getPhysicalBookId();
                goToBookDetail(view, livros.get(position));
            }
        });

    }

    public void goToBookDetail(View view, Book book) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
//        intent.putExtra("p_fbook_id", fbook_id);
        PrefUtils.setCurrentBook(book, getActivity().getApplicationContext());
        startActivity(intent);
    }

    public void goToBookAdd() {
        Intent intent = new Intent(getActivity(), BookAddActivity.class);
        startActivity(intent);
    }

}

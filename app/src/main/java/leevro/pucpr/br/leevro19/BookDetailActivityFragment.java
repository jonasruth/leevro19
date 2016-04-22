package leevro.pucpr.br.leevro19;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import leevro.pucpr.br.leevro19.entity.Book;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookDetailActivityFragment extends Fragment {

    TextView bookTitle;
    TextView bookAuthorName;
    TextView bookGenderName;
    TextView bookDescription;
    ImageView bookCover;
    String photo;
    Book livro;

    private String physicalBookId;

    public BookDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        // INICIO MEU CODIGO

        Bundle extras = getActivity().getIntent().getExtras();
        String p_fbook_id = null;
        String p_user_id = null;

        if (extras != null) {
            p_fbook_id = extras.getString("p_fbook_id");
            p_user_id = extras.getString("p_user_id");
        }


        // começa a parada
        bookTitle = (TextView) view.findViewById(R.id.bookTitle);
        bookAuthorName = (TextView) view.findViewById(R.id.bookAuthorName);
        bookGenderName = (TextView) view.findViewById(R.id.bookGenderName);
        bookDescription = (TextView) view.findViewById(R.id.bookDescription);
        bookCover = (ImageView) view.findViewById(R.id.bookCover);

        String url = "http://96.126.115.143/leevrows/retornaUmLivroFisico.php?fbook_id=" + p_fbook_id;

        Log.d("URL:::",url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {
                            Boolean status = response.getBoolean("status");
                            if (status) {
                                livro = new Book(response.getJSONObject("livro"));
                                bookTitle.setText(livro.getTitle());
                                bookAuthorName.setText(livro.getAuthorName());
                                bookGenderName.setText(livro.getGenderName());
                                bookDescription.setText(livro.getDescription());
//                            bookPhoto.setText(livro.getString("photo"));
                                photo = livro.getPhoto();
                            }else{
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Erro: Não foi possível retornar livro.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
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
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
        Volley.newRequestQueue(getActivity()).add(jsObjRequest);
        // FIM MEU CODIGO

        return view;
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
        Volley.newRequestQueue(getActivity()).add(request);
    }

}

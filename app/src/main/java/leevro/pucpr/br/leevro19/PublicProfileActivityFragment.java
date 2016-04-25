package leevro.pucpr.br.leevro19;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class PublicProfileActivityFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private AppUser usuario;
    private AppUser loggedUser;


    TextView txtUserName;
    NetworkImageView niv;

    public PublicProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_public_profile, container, false);

        // Inicio do meu código

        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        niv = (NetworkImageView) view.findViewById(R.id.profileImage);


        usuario = PrefUtils.getCurrentPublicProfile(getActivity().getApplicationContext());
        loggedUser = PrefUtils.getCurrentUser(getActivity().getApplicationContext());

        Log.d("XXXX",usuario.userId);
        Log.d("YYYY",PrefUtils.getCurrentUser(getActivity().getApplicationContext()).userId);
        if(usuario.userId.equals(PrefUtils.getCurrentUser(getActivity().getApplicationContext()).userId)){

            view.findViewById(R.id.btnAdicionarLivroSeparator).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btnAdicionarLivro).setVisibility(View.VISIBLE);
        }else{

            view.findViewById(R.id.btnAdicionarLivroSeparator).setVisibility(View.GONE);
            view.findViewById(R.id.btnAdicionarLivro).setVisibility(View.GONE);
        }

        Map<String, String> params = new HashMap();
        params.put("user_id", usuario.userId);
        JSONObject parameters = new JSONObject(params);
        Log.d("Request: ", parameters.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_GET_USER, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {
//                            listaLivros = response.getJSONArray("livros");
                            JSONObject juser = response.getJSONObject("user");
                            usuario.userId = juser.getString("id");
                            usuario.facebookID = juser.getString("fb_facebook_id");

                            if(!usuario.userId.equals(loggedUser.userId)) {
                                usuario.name = juser.getString("fb_first_name");
                            }else{
                                usuario.name = juser.getString("fb_name");
                            }

                            PrefUtils.setCurrentPublicProfile(usuario,getActivity().getApplicationContext() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new VolleyCallback() {
                            @Override
                            public void onSuccess() {
                                preencherCamposFragment();
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

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsObjRequest);


//         Fim do meu código

        return view;
    }
    public interface VolleyCallback {
        void onSuccess();
    }

    private void preencherCamposFragment(){
        Context ctx = getActivity().getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(ctx);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        niv.setImageUrl( AppUtils.APP_PATH_USER_PIC_PATH + usuario.facebookID.toString()+".jpg",mImageLoader);
        Log.d("profile image >>>", AppUtils.APP_PATH_USER_PIC_PATH + usuario.facebookID.toString()+".jpg");
        txtUserName.setText(usuario.name);
    }
}

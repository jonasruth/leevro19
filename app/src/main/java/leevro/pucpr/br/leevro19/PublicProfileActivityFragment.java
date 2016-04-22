package leevro.pucpr.br.leevro19;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class PublicProfileActivityFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public PublicProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_public_profile, container, false);

//        Log.d("xxxx",this.getArguments().toString());

        // Inicio do meu código

        Bundle extras = getActivity().getIntent().getExtras();
        String p_fbook_id = null;
        String p_user_id = null;

        if (extras != null) {
            p_fbook_id = extras.getString("p_fbook_id");
            p_user_id = extras.getString("p_user_id");
        }
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

        NetworkImageView niv = (NetworkImageView) view.findViewById(R.id.profileImage);
        niv.setImageUrl("http://96.126.115.143/leevrows/user_img/" + PrefUtils.getCurrentUser(ctx).facebookID.toString()+".jpg",mImageLoader);
        Log.d("profile image >>>", "http://96.126.115.143/leevrows/user_img/" + PrefUtils.getCurrentUser(ctx).facebookID.toString()+".jpg");
        TextView txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtUserName.setText(PrefUtils.getCurrentUser(getActivity().getApplicationContext()).name);
//         Fim do meu código

        return view;
    }
}

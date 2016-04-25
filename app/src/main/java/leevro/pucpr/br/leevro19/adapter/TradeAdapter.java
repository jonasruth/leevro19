package leevro.pucpr.br.leevro19.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import leevro.pucpr.br.leevro19.R;
import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.entity.Book;
import leevro.pucpr.br.leevro19.entity.BookCollection;
import leevro.pucpr.br.leevro19.entity.Trade;
import leevro.pucpr.br.leevro19.entity.TradeCollection;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

/**
 * Created by Jonas on 12/04/2016.
 */
public class TradeAdapter extends BaseAdapter {

    private Context context;
    private final TradeCollection trades;
    private static LayoutInflater inflater = null;
    private AppUser user;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public TradeAdapter(Context context, TradeCollection trades) {
        super();
        this.context = context;
        user = PrefUtils.getCurrentPublicProfile(context.getApplicationContext());
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.trades = trades;
    }

    public TradeAdapter(Context context, JSONArray trades) {
        super();
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TradeCollection temp = new TradeCollection();
        int length = trades.length();

        if (length > 0) {
            for (int i = 0; i < length; i++) {
                Trade item = new Trade();
                /*try {
                    item.setFirstUser();
                    item.setSecondUser();
                    item.setFirstUserRequestedBook();
                    item.setSecondUserRequestedBook();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                temp.add(item);
            }
        }
        this.trades = temp;

    }


    @Override
    public int getCount() {
        return trades.size();
    }

    @Override
    public Object getItem(int position) {
        return trades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        //ImageView img;
        TextView availability;
        TextView requests;
        TextView txtRequests;
        NetworkImageView img;
        TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.trade_list, null);

        mRequestQueue = Volley.newRequestQueue(rowView.getContext());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        Holder holder = new Holder();
        Trade trade = trades.get(position);

//        holder.availability = (TextView) rowView.findViewById(R.id.availability);
//        holder.requests = (TextView) rowView.findViewById(R.id.requests);
//        holder.txtRequests = (TextView) rowView.findViewById(R.id.txtRequests);
//        holder.img = (NetworkImageView) rowView.findViewById(R.id.imageView1);
//        holder.tv = (TextView) rowView.findViewById(R.id.textView1);

//        AppUser loggedUser = PrefUtils.getCurrentUser(context.getApplicationContext());

        /*
        if(!user.userId.equals(loggedUser.userId) || trade.getRequests()<1) {
            holder.requests.setVisibility(TextView.GONE);
            holder.txtRequests.setVisibility(TextView.GONE);
        }

        holder.availability.setText(trade.getAvailability());
        holder.requests.setText(trade.getRequests().toString());
        holder.img.setImageUrl(AppUtils.APP_PATH_VIRTUAL_BOOK_COVER_PATH + "tiny_" + trade.getPhoto(), mImageLoader);
        holder.tv.setText(trade.getTitle());*/

        return rowView;
    }

}

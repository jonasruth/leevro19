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
import leevro.pucpr.br.leevro19.network.CustomVolleyRequestQueue;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

/**
 * Created by Jonas on 12/04/2016.
 */
public class BookAdapter extends BaseAdapter {

    private Context context;
    private final BookCollection books;
    private static LayoutInflater inflater = null;
    private AppUser targetUser;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public BookAdapter(Context context, BookCollection books) {
        super();
        this.context = context;
        targetUser = PrefUtils.getTargetUser(context.getApplicationContext());
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.books = books;
    }

    public BookAdapter(Context context, JSONArray books) {
        super();
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        BookCollection temp = new BookCollection();
        int length = books.length();

        if (length > 0) {
            for (int i = 0; i < length; i++) {
                Book item = new Book();
                try {
                    item.setTitle(books.getJSONObject(i).getString("title"));
                    item.setPhoto(books.getJSONObject(i).getString("photo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                temp.add(item);
            }
        }
        this.books = temp;

    }


    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
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
        View rowView = inflater.inflate(R.layout.book_list, null);

        Holder holder = new Holder();
        Book book = books.get(position);

        holder.availability = (TextView) rowView.findViewById(R.id.availability);
        holder.requests = (TextView) rowView.findViewById(R.id.requests);
        holder.txtRequests = (TextView) rowView.findViewById(R.id.txtRequests);
        holder.img = (NetworkImageView) rowView.findViewById(R.id.imageView1);
        holder.tv = (TextView) rowView.findViewById(R.id.textView1);

        AppUser loggedUser = PrefUtils.getLoggedUser(context.getApplicationContext());

        if(!targetUser.userId.equals(loggedUser.userId) || book.getRequests()<1) {
            holder.requests.setVisibility(TextView.GONE);
            holder.txtRequests.setVisibility(TextView.GONE);
        }

        holder.availability.setText(book.getAvailability());
        holder.requests.setText(book.getRequests().toString());
        holder.tv.setText(book.getTitle());

        mImageLoader = CustomVolleyRequestQueue.getInstance(context.getApplicationContext()).getImageLoader();
        holder.img.setImageUrl(AppUtils.APP_PATH_VIRTUAL_BOOK_COVER_PATH + "tiny_" + book.getPhoto(), mImageLoader);


        return rowView;
    }

}

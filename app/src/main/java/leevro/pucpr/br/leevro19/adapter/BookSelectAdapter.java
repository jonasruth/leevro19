package leevro.pucpr.br.leevro19.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import leevro.pucpr.br.leevro19.R;
import leevro.pucpr.br.leevro19.entity.Book;
import leevro.pucpr.br.leevro19.entity.BookCollection;
import leevro.pucpr.br.leevro19.utils.AppUtils;

/**
 * Created by Jonas on 12/04/2016.
 */
public class BookSelectAdapter extends BaseAdapter {

    private Context context;
    private final BookCollection books;
    private static LayoutInflater inflater = null;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public BookSelectAdapter(Context context, BookCollection books) {
        super();
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.books = books;
    }

    public BookSelectAdapter(Context context, JSONArray books) {
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
        CheckBox chk;
        NetworkImageView img;
        TextView tv;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.book_select_list, null);

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
        Book book = books.get(position);

        holder.chk = (CheckBox) rowView.findViewById(R.id.chk);
        holder.img = (NetworkImageView) rowView.findViewById(R.id.imageView1);
        holder.tv = (TextView) rowView.findViewById(R.id.textView1);
        holder.img.setImageUrl("http://96.126.115.143/leevrows/vbook_img/tiny_" + book.getPhoto(),mImageLoader);
        holder.tv.setText(book.getTitle());

        holder.chk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Boolean x = convertView.findViewById(R.id.chk).isSelected();

                Toast.makeText(context,"ImageClickClick " + x, Toast.LENGTH_LONG).show();
            }
        });


        return rowView;
    }

}

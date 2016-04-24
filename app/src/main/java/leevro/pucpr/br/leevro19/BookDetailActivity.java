package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import leevro.pucpr.br.leevro19.entity.Book;

public class BookDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // tabs
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("TAB 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Livro");


        TabSpec spec2=tabHost.newTabSpec("TAB 2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Usuário");


        tabHost.addTab(spec1);
        tabHost.addTab(spec2);

    }

}

package leevro.pucpr.br.leevro19;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.barcodescanner.BarcodeTrackerFactory;
import leevro.pucpr.br.leevro19.barcodescanner.CameraSourcePreview;
import leevro.pucpr.br.leevro19.barcodescanner.GraphicOverlay;
import leevro.pucpr.br.leevro19.barcodescanner.GraphicTracker;
import leevro.pucpr.br.leevro19.lib.ISBN;
import leevro.pucpr.br.leevro19.lib.InvalidStandardIDException;
import leevro.pucpr.br.leevro19.network.CustomVolleyRequestQueue;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class BookAddActivity extends ActionBarActivity {

    CameraSourcePreview mPreview;
    CameraSource mCameraSource;
    GraphicOverlay mGraphicOverlay;

    TextView find;

    EditText isbn;
    TextView bookTitle;
    TextView bookAuthorName;
    TextView bookGenderName;
    TextView bookDescription;
    TextView bookYear;
    TextView bookLanguage;
    //    TextView bookPhoto;
    TextView bookEdition;
    NetworkImageView bookCover;
    String photo;
    LinearLayout bookDetailPreview;
    LinearLayout consulta;
    JSONObject livro;
    private ImageLoader mImageLoader;
    private String barcodeScanISBN;
    CameraSourcePreview cameraSourcePreview;
    FrameLayout frag;

    //    ActionBar actionBar;
    ActionBar actionBar;

    LinearLayout pesquisaCodigoBarras;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);
//        configureBarcodeScanner();

        consulta = (LinearLayout) findViewById(R.id.consulta);
        bookDetailPreview = (LinearLayout) findViewById(R.id.bookDetailPreview);
        consulta.setVisibility(LinearLayout.VISIBLE);
        bookDetailPreview.setVisibility(ScrollView.GONE);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        isbn = (EditText) findViewById(R.id.isbn);
        find = (TextView) findViewById(R.id.find);

//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                mMessageReceiver, new IntentFilter("ISBNScanner"));

        pesquisaCodigoBarras = (LinearLayout) findViewById(R.id.pesquisaCodigoBarras);

//        configureBarcodeScanner();

    }

    public void configureBarcodeScanner() {
        mPreview = (CameraSourcePreview) findViewById(R.id.cameraSourcePreview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.overlay);

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(
                getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, new GraphicTracker.Callback() {
            @Override
            public void onFound(final String barcodeValue) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Log.d("ISBN", barcodeValue);
                            ISBN isbnObj = new ISBN(barcodeValue.toString());
//                            isbn.setText(isbnObj.toString(true));
                            goToBookPropose(barcodeValue.toString());
                        } catch (InvalidStandardIDException e) {
                            Log.d("ISBN", e.getMessage());
                        }

                    }
                });

//                final Handler handler = new Handler(Looper.getMainLooper());
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                try {
//                    Log.d("ISBN", barcodeValue);
//                    ISBN isbnObj = new ISBN(barcodeValue.toString());
////                            isbn.setText(isbnObj.toString(true));
//                    goToBookPropose(isbnObj.toString());
//                    return;
//                } catch (InvalidStandardIDException e) {
//                    Log.d("ISBN", e.getMessage());
//                }
//                    }
//                }, 2000);

//                PrefUtils.clearCurrentISBN(getApplicationContext());
            }
//                }, 2000);
        });
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //Integer width = size.x;
        //Integer height = size.y;
        Integer width = size.x;
        Integer height = 300;
        Log.d("screenX x screenY", width.toString() + 'x' + height.toString());

        mCameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(width, height)
                .setRequestedFps(30.0f)
                .build();
    }

    public void stopBarcodeScanner() {
        Log.d("stopBarcodeScanner", "CALLED");
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    public void lerCodigoDeBarras(View view) {
        configureBarcodeScanner();
        startCameraSource();
//        mPreview = (CameraSourcePreview) findViewById(R.id.cameraSourcePreview);
//        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.overlay);
        mPreview.setVisibility(View.VISIBLE);
        mGraphicOverlay.setVisibility(View.VISIBLE);
        pesquisaCodigoBarras.setVisibility(View.GONE);
//        configureBarcodeScanner();
    }

    public void goToBookPropose(View view) {
        AppUtils.esconderTecladoVirtual(this);
        goToBookPropose(isbn.getText().toString());
    }

    public void goBack(View view) {
        actionBar.show();
        consulta.setVisibility(LinearLayout.VISIBLE);
        bookDetailPreview.setVisibility(LinearLayout.GONE);
        isbn.setText(null);
    }

    private void goToBookPropose(String isbnToSearch) {


        try {
            new ISBN(isbnToSearch);


            stopBarcodeScanner();
            if (mPreview != null) {
                mPreview.setVisibility(View.GONE);
            }
            if (mGraphicOverlay != null) {
                mGraphicOverlay.setVisibility(View.GONE);
            }
            pesquisaCodigoBarras.setVisibility(View.VISIBLE);

            bookTitle = (TextView) findViewById(R.id.bookTitle);
            bookAuthorName = (TextView) findViewById(R.id.bookAuthorName);
            bookGenderName = (TextView) findViewById(R.id.bookGenderName);
            bookDescription = (TextView) findViewById(R.id.bookDescription);
            bookEdition = (TextView) findViewById(R.id.bookEdition);
            bookCover = (NetworkImageView) findViewById(R.id.bookCover);

            String url = "http://96.126.115.143/leevrows/retornaUmLivro.php?isbn=" + isbnToSearch;
            Log.d("Consulta: ", "http://96.126.115.143/leevrows/retornaUmLivro.php?isbn=" + isbnToSearch);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Retorno: ", response.toString());

                            try {

                                Boolean status = response.getBoolean("status");
                                if (status) {


                                    livro = response.getJSONObject("livro");
                                    barcodeScanISBN = livro.getString("isbn");
                                    bookTitle.setText(livro.getString("title"));
                                    bookAuthorName.setText(livro.getString("author_name"));
                                    bookGenderName.setText(livro.getString("gender_name"));
                                    bookDescription.setText(livro.getString("description"));
                                    bookEdition.setText(livro.getString("edition"));
                                    photo = livro.getString("photo");

                                    mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getImageLoader();
                                    bookCover.setImageUrl(AppUtils.APP_PATH_VIRTUAL_BOOK_COVER_PATH + livro.getString("photo"), mImageLoader);

                                    consulta.setVisibility(LinearLayout.GONE);
                                    bookDetailPreview.setVisibility(LinearLayout.VISIBLE);

                                    actionBar.hide();

                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Desculpe, não encontramos seu livro ainda... :(", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                            } catch (
                                    JSONException e
                                    )

                            {
                                e.printStackTrace();
                            }
                        }
                    }

                            , new Response.ErrorListener()

                    {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Erro: ", error.toString());
//                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
//                        toast.show();

                        }
                    }

                    );
            Volley.newRequestQueue(this).add(jsObjRequest);

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    3,
                    2f));
        } catch (InvalidStandardIDException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Preencha corretamente o ISBN ou utilize o leitor de código de barras", Toast.LENGTH_SHORT).show();
        }
        //Volley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void addBook(View view) {
        addBook();
    }

    public void addBook() {
        String url = "http://96.126.115.143/leevrows/adicionaUmLivro.php";

        Map<String, String> params = new HashMap();
        params.put("isbn", barcodeScanISBN);
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        params.put("photo", photo);
        JSONObject parameters = new JSONObject(params);

        Log.d("addBook", parameters.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());

                        try {
                            JSONObject status = response.getJSONObject("status");
                            Boolean sucesso = status.getBoolean("success");
                            if (sucesso) {
                                Intent intent = new Intent(BookAddActivity.this, BookGalleryActivity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        //Volley.getInstance(this).addToRequestQueue(jsObjRequest);
    }


    private void startCameraSource() {
        try {
            mPreview.start(mCameraSource, mGraphicOverlay);
        } catch (IOException e) {
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startCameraSource(); //start
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop(); //stop
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }//release the resources
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

}

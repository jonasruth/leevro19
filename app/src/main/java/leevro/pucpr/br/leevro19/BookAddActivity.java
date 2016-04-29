package leevro.pucpr.br.leevro19;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    ActionBar actionBar;

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

        configureBarcodeScanner();

    }

    public void configureBarcodeScanner() {
        mPreview = (CameraSourcePreview) findViewById(R.id.cameraSourcePreview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.overlay);

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(
                getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, new GraphicTracker.Callback() {
            @Override
            public void onFound(final String barcodeValue) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ISBN isbnObj = new ISBN(barcodeValue.toString());
//                            barcodeScanISBN = barcodeValue.toString();
//                isbn.setText(barcodeScanISBN);
                            //goToBookPropose(isbnObj.toString());
//                            Log.d("BookAdd::onFound", isbnObj.toString());
//                            isbn.setText(isbnObj.toString(true));


//                            mPreview.release();
//                            mPreview.stop();

                        } catch (InvalidStandardIDException e) {
                            Log.d("ISBN", e.getMessage());
                        }
                    }
                }, 500);

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
                .setRequestedFps(5.0f)
                .build();
    }



            /*private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    String scannedISBN = intent.getStringExtra("ISBN");
//            String scannedISBN = (String) b.getParcelable("ISBN");
                    if (scannedISBN != null && !scannedISBN.isEmpty()) {
                        barcodeScanISBN = scannedISBN;
                        Log.d("BookAddActivity 1", barcodeScanISBN);
                        //goToBookPropose(barcodeScanISBN);
                        isbn.setText(barcodeScanISBN);
                        goToBookPropose(barcodeScanISBN);
                        //find.callOnClick();
                        Log.d("BookAddActivity 2", barcodeScanISBN);
                        PrefUtils.clearCurrentISBN(getApplicationContext());
                    } else {
                        Log.d("BookAddActivity", "ISBN NULO");
                    }

                    frag.setVisibility(FrameLayout.GONE);

                }
            };*/


    public void lerCodigoDeBarras(View view) {

    }

    public void goToBookPropose(View view) {
//        AppUtils.esconderTecladoVirtual(this);
        goToBookPropose(isbn.getText().toString());
    }

    public void goBack(View view) {
        actionBar.show();
        consulta.setVisibility(LinearLayout.VISIBLE);
        bookDetailPreview.setVisibility(LinearLayout.GONE);
        isbn.setText(null);
    }

    private void goToBookPropose(String isbnToSearch) {
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthorName = (TextView) findViewById(R.id.bookAuthorName);
        bookGenderName = (TextView) findViewById(R.id.bookGenderName);
        bookDescription = (TextView) findViewById(R.id.bookDescription);
        bookEdition = (TextView) findViewById(R.id.bookEdition);
        bookCover = (NetworkImageView) findViewById(R.id.bookCover);

        String url = "http://96.126.115.143/leevrows/retornaUmLivro.php?isbn=" + isbnToSearch;
        Log.d("Retorno: ", "xxadadas");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {

                            Boolean status = response.getBoolean("status");
                            if (status) {


                                livro = response.getJSONObject("livro");
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
                                Toast toast = Toast.makeText(getApplicationContext(), "Erro: Não foi possível retornar livro pesquisado.", Toast.LENGTH_SHORT);
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
                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }

                );
        Volley.newRequestQueue(this).add(jsObjRequest);

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                2f));
        //Volley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void addBook(View view) {
        addBook();
    }

    public void addBook() {
        String url = "http://96.126.115.143/leevrows/adicionaUmLivro.php";

        Map<String, String> params = new HashMap();
        params.put("isbn", isbn.getText().toString());
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        params.put("photo", photo);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());

                        try {
                            JSONObject status = response.getJSONObject("status");
                            Boolean sucesso = status.getBoolean("success");

                            Intent intent = new Intent(BookAddActivity.this, BookGalleryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro: ", error.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

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
        startCameraSource(); //start
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop(); //stop
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraSource.release(); //release the resources
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

}

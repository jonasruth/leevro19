package leevro.pucpr.br.leevro19;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import leevro.pucpr.br.leevro19.customview.CameraSourcePreview;
import leevro.pucpr.br.leevro19.customview.GraphicOverlay;
import leevro.pucpr.br.leevro19.custom.BarcodeTrackerFactory;


public class BarcodeScannerActivity extends AppCompatActivity {

    CameraSourcePreview mPreview;
    CameraSource mCameraSource;
    GraphicOverlay mGraphicOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.overlay);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(
                getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());


        mCameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .build();


//        ImageRequest request = new ImageRequest(
//                "http://aspasevirgulas.com.br/wordpress/wp-content/uploads/2016/02/isbn-gute.jpg",
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap bitmap) {
//                        Log.d("OnResponse Barcode:","Respondeu");
//                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//                        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).build();
//                        SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
//                        if (barcodes.size() != 0) {
//                            Barcode result = barcodes.valueAt(0);
//                            Log.d("barcode:", result.rawValue);
//                            Toast.makeText(getApplicationContext(), result.rawValue, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Log.d("barcode:", "num deu");
//                            Toast.makeText(getApplicationContext(), "num deu", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, 0, 0, null,
//                new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        //bookCover.setImageResource(R.drawable.image_load_error);
//                        Log.e("Erro barcode:",error.toString());
//                    }
//                });
//        Volley.newRequestQueue(this).add(request);
    }

    //starting the preview

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
    }


}

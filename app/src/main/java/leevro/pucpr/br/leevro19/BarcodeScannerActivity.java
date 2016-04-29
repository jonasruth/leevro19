package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import leevro.pucpr.br.leevro19.barcodescanner.GraphicTracker;
import leevro.pucpr.br.leevro19.barcodescanner.CameraSourcePreview;
import leevro.pucpr.br.leevro19.barcodescanner.GraphicOverlay;
import leevro.pucpr.br.leevro19.barcodescanner.BarcodeTrackerFactory;
import leevro.pucpr.br.leevro19.lib.ISBN;
import leevro.pucpr.br.leevro19.lib.InvalidStandardIDException;
import leevro.pucpr.br.leevro19.utils.PrefUtils;


public class BarcodeScannerActivity extends AppCompatActivity {

    CameraSourcePreview mPreview;
    CameraSource mCameraSource;
    GraphicOverlay mGraphicOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_barcode_scanner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPreview = (CameraSourcePreview) findViewById(R.id.cameraSourcePreview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.overlay);

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(
                getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, new GraphicTracker.Callback() {
            @Override
            public void onFound(final String barcodeValue) {

                try {

                    ISBN isbnObj = new ISBN(barcodeValue);

                    PrefUtils.setCurrentISBN(isbnObj.toString(true), getApplicationContext());
                    // Do something after 5s = 5000ms
                    Intent intent = new Intent(BarcodeScannerActivity.this, BookAddActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (InvalidStandardIDException e) {
                    Log.d("ISBN", e.getMessage());
                }

//                final Handler handler = new Handler(Looper.getMainLooper());
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        PrefUtils.setCurrentISBN(barcodeValue,getApplicationContext());
//                        // Do something after 5s = 5000ms
//                        Intent intent = new Intent(BarcodeScannerActivity.this,BookAddActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    }
//                }, 2000);
//
//
//                Log.d("CÓDIGO DE BARRAS", barcodeValue);
//                Toast.makeText(getApplicationContext(),barcodeValue,Toast.LENGTH_SHORT).show();
            }
        });
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Integer width = size.x;
        Integer height = size.y;
        Log.d("screenX x screenY", width.toString() + 'x' + height.toString());

        mCameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(width, height)
                .build();

    }
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

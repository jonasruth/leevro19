package leevro.pucpr.br.leevro19;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import leevro.pucpr.br.leevro19.barcodescanner.BarcodeTrackerFactory;
import leevro.pucpr.br.leevro19.barcodescanner.CameraSourcePreview;
import leevro.pucpr.br.leevro19.barcodescanner.GraphicOverlay;
import leevro.pucpr.br.leevro19.barcodescanner.GraphicTracker;
import leevro.pucpr.br.leevro19.lib.ISBN;
import leevro.pucpr.br.leevro19.lib.InvalidStandardIDException;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class BarcodeScannerActivityFragment extends Fragment {

    CameraSourcePreview mPreview;
    CameraSource mCameraSource;
    GraphicOverlay mGraphicOverlay;

    private static final String TAG = "SCANNER";

    public BarcodeScannerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);

        //


//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_barcode_scanner);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPreview = (CameraSourcePreview) view.findViewById(R.id.cameraSourcePreview);
        mGraphicOverlay = (GraphicOverlay) view.findViewById(R.id.overlay);

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(
                getActivity().getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, new GraphicTracker.Callback() {
            @Override
            public void onFound(final String barcodeValue) {

                try {
                    ISBN isbnObj = new ISBN(barcodeValue);
                    PrefUtils.setCurrentISBN(isbnObj.toString(true), getActivity().getApplicationContext());
                    sendMessageToActivity(getActivity().getApplicationContext(), isbnObj.toString());
                    mPreview.stop();

//                    onStop();
//                            onDestroyView();
                } catch (InvalidStandardIDException e) {
                    Log.d("ISBN", e.getMessage());
                }


//                try {
//
//                    ISBN isbnObj = new ISBN(barcodeValue);
//
//                    PrefUtils.setCurrentISBN(isbnObj.toString(true), getActivity().getApplicationContext());
//                    // Do something after 5s = 5000ms
////                    Intent intent = new Intent(getActivity(), BookAddActivity.class);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                    startActivity(intent);
////                    getActivity().finish();
//                } catch (InvalidStandardIDException e) {
//                    Log.d("ISBN", e.getMessage());
//                }

//                final Handler handler = new Handler(Looper.getMainLooper());
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        try {
//                            ISBN isbnObj = new ISBN(barcodeValue);
//                            PrefUtils.setCurrentISBN(isbnObj.toString(true), getActivity().getApplicationContext());
//                            sendMessageToActivity(getActivity().getApplicationContext(),isbnObj.toString());
////                            onDestroyView();
//                        } catch (InvalidStandardIDException e) {
//                            Log.d("ISBN", e.getMessage());
//                        }
//
////                        PrefUtils.setCurrentISBN(barcodeValue,getApplicationContext());
////                        // Do something after 5s = 5000ms
////                        Intent intent = new Intent(BarcodeScannerActivity.this,BookAddActivity.class);
////                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                        startActivity(intent);
////                        finish();
//                    }
//                }, 2000);


                //Log.d("CÓDIGO DE BARRAS", barcodeValue);
//                Toast.makeText(getApplicationContext(),barcodeValue,Toast.LENGTH_SHORT).show();
            }
        });
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //Integer width = size.x;
        //Integer height = size.y;
        Integer width = size.x;
        Integer height = 300;
        Log.d("screenX x screenY", width.toString() + 'x' + height.toString());

        mCameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(width, height)
                .build();


        //

        return view;


    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();

    }

    private static void sendMessageToActivity(Context ctx, String isbn) {
        Intent intent = new Intent("ISBNScanner");
        // You can also include some extra data.
        intent.putExtra("ISBN", isbn);
//        Bundle b = new Bundle();
////        b.putParcelable("ISBN", isbnObj);
//        intent.putExtra("ISBN", b);
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
    }

    public void startCameraSource() {
        try {
            mPreview.start(mCameraSource, mGraphicOverlay);
        } catch (IOException e) {
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource(); //start
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreview.stop(); //stop
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        mCameraSource.release(); //release the resources
    }
}

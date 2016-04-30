package leevro.pucpr.br.leevro19.service;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.BookGalleryActivity;
import leevro.pucpr.br.leevro19.MyProfile;
import leevro.pucpr.br.leevro19.lib.GPSStatus;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;
//import android.widget.Toast;

public class ServiceGPS extends Service {

    private static final String TAG = "BOOMBOOMTESTGPS";
    private static final int LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = 10f;

    private LocationManager mLocationManager = null;
    private GPSStatus gpsStatus = new GPSStatus();


    public ServiceGPS() {
    }

    private void checkNotifications() {
        Map<String, String> params = new HashMap();
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_CHECK_NOTIFICATIONS, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("checkNotifications", response.toString());
                        JSONArray arr = null;
                        try {
                            arr = response.getJSONArray("notifications");
                            if(arr.length()>0) {
                                sendNotification(getApplicationContext(), arr.getJSONObject(0).getString("message"));
                            }
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
    }

    private static void sendNotification(Context ctx, String message) {
        Intent intent = new Intent("LeevroNotification");
        // You can also include some extra data.
        intent.putExtra("Message", message);
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
    }

    private static void sendMessageToActivity(final Context ctx, Location l, boolean providerEnabled) {

        Map<String, String> params = new HashMap();
        params.put("user_id", PrefUtils.getLoggedUser(ctx).userId);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_CHECK_NOTIFICATIONS, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("checkNotifications", response.toString());
                        JSONArray arr = null;
                        try {
                            arr = response.getJSONArray("notifications");
                            if(arr.length()>0) {
                                for(int i=0;i<arr.length();i++) {
                                    sendNotification(ctx, arr.getJSONObject(i).getString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro: ", error.toString());
                        Toast toast = Toast.makeText(ctx, "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
        Volley.newRequestQueue(ctx).add(jsObjRequest);

        Log.d("MSG:GPSLocationUpdates", "provider enabled:" + providerEnabled + " - location" + l.toString());
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
        intent.putExtra("ProviderEnabled", providerEnabled);
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
    }


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        boolean providerEnabled;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            checkNotifications();
            Log.e(TAG, "onLocationChanged: " + location);
//            Toast.makeText(getApplicationContext(),"onLocationChanged: " + location,Toast.LENGTH_SHORT).show();
            mLastLocation.set(location);
            sendMessageToActivity(getApplicationContext(), mLastLocation, providerEnabled);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            providerEnabled = Boolean.FALSE;
            sendMessageToActivity(getApplicationContext(), mLastLocation, providerEnabled);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
//            Toast.makeText(getApplicationContext(),"onProviderEnabled: " + provider,Toast.LENGTH_SHORT).show();
            providerEnabled = Boolean.TRUE;
            sendMessageToActivity(getApplicationContext(), mLastLocation, providerEnabled);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
            checkNotifications();
//            Toast.makeText(getApplicationContext(),"onStatusChanged: " + provider,Toast.LENGTH_SHORT).show();
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
//        Toast.makeText(getApplicationContext(),"onStartCommand ",Toast.LENGTH_SHORT).show();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
//        Toast.makeText(getApplicationContext(),"onCreate ",Toast.LENGTH_SHORT).show();
        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
//        Toast.makeText(getApplicationContext(),"initializeLocationManager",Toast.LENGTH_SHORT).show();
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


}

package leevro.pucpr.br.leevro19;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.entity.Book;
import leevro.pucpr.br.leevro19.entity.BookCollection;
import leevro.pucpr.br.leevro19.entity.BookFeeder;
import leevro.pucpr.br.leevro19.service.ServiceGPS;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class MainActivity extends ActionBarActivity {

    //    boolean mBounded;
    ServiceGPS mServer;

    private AppUser loggedUser;
    private TextView txtQuestion;
    private LinearLayout bookLoadInfo;
    private LinearLayout noBooksInfo;
    private ImageView bookCover;
    private TextView txtDistance;
    private LinearLayout bookCoverContainer;
    private TableLayout choiceButtonsContainer;
    private BookCollection listaLivros;
    private Book livroAtual;
    private int bookListIndex = -1;
    private Boolean carregando = false;
    Location myLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loggedUser = PrefUtils.getLoggedUser(getApplicationContext());

        txtQuestion = (TextView) findViewById(R.id.question);
        txtQuestion.setText(loggedUser.firstName + ", gostaria de ler este livro?");

//        Toast.makeText(getApplicationContext(), PrefUtils.getLoggedUser(this).email, Toast.LENGTH_SHORT).show();

        bookLoadInfo = (LinearLayout) findViewById(R.id.bookLoadInfo);
        noBooksInfo = (LinearLayout) findViewById(R.id.noBooksInfo);
        bookCoverContainer = (LinearLayout) findViewById(R.id.bookCoverContainer);
        bookCover = (ImageView) findViewById(R.id.bookCover);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        choiceButtonsContainer = (TableLayout) findViewById(R.id.choiceButtonsContainer);

        bookLoadInfo.setVisibility(TextView.GONE);
        bookCoverContainer.setVisibility(ImageView.GONE);
        noBooksInfo.setVisibility(TextView.GONE);
        choiceButtonsContainer.setVisibility(TextView.INVISIBLE);

        LocalBroadcastManager.getInstance(this).registerReceiver( mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
        LocalBroadcastManager.getInstance(this).registerReceiver( mNotificationReceiver, new IntentFilter("LeevroNotification"));


        startService(new Intent(MainActivity.this, ServiceGPS.class));

        loadBookListForChoice();
    }

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intentA) {

            String message = intentA.getStringExtra("Message");

            // Prepare intent which is triggered if the
            // notification is selected
            Intent intent = new Intent(MainActivity.this, BookGalleryActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);

            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Build notification
            // Actions are just fake
            Notification.Builder xxxx = new Notification.Builder(getApplicationContext());
            xxxx
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message).setSmallIcon(R.mipmap.ic_launcher_dev)
                    .setContentIntent(pIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//             hide the notification after its selected
//            xxxx.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, xxxx.getNotification());

        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MSG GPS", "CHEGOU");

            // Get extra data included in the Intent
            Boolean providerEnabled = intent.getBooleanExtra("ProviderEnabled", false);
            Bundle b = intent.getBundleExtra("Location");
            myLocation = (Location) b.getParcelable("Location");

            Log.d("myLocation", myLocation.toString());

            if (myLocation != null) {

                Log.d("TentandoSalvarPOS", "TentandoSalvarPOS");
                salvarPosicao();
                if (livroAtual != null) {
                    Location bookLocation = new Location("");
                    bookLocation.setLatitude(livroAtual.getLat());
                    bookLocation.setLongitude(livroAtual.getLng());
                    if (myLocation != null) {
                        Float distance = myLocation.distanceTo(bookLocation);
                        Log.d("lat/lng:", myLocation.getLatitude() + "/" + myLocation.getLongitude());
                        String distanceStr;
                        if(distance<2000) {
                            distanceStr = Math.round(distance) + " metros";
                        }else{
                            distanceStr = Math.round(distance / 1000) + " km";
                        }
                        txtDistance.setText("Está a " + distanceStr +" de você");
                    } else {
                        txtDistance.setText("");
                    }
                }


            }
//            tvStatus.setText(message);
//            T
        }
    };

    public void salvarPosicao() {

        Log.d("UpdateLocation:request", "1xxx");

        Map<String, String> params = new HashMap();
        params.put("user_id", loggedUser.userId);
        params.put("geo_last_lat", String.valueOf(myLocation.getLatitude()));
        params.put("geo_last_lng", String.valueOf(myLocation.getLongitude()));
        JSONObject parameters = new JSONObject(params);

        Log.d("UpdateLocation:request", parameters.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppUtils.APP_URL_WS_UPD_USER_LAST_LOCATION,
                parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("UpdateLocation:request", response.toString());
//                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                try {
                    JSONObject status = response.getJSONObject("status");
                    Boolean success = status.getBoolean("success");
                    Log.d("UpdateLocation", response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("UpdateLocation:except", e.getMessage());
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


    public void loadBooks(View view) {
        loadBookListForChoice();
    }

    private void loadBookListForChoice() {

        bookListIndex = -1;
        carregando = true;
        bookLoadInfo.setVisibility(TextView.VISIBLE);
        bookCoverContainer.setVisibility(ImageView.GONE);
        noBooksInfo.setVisibility(TextView.GONE);
        choiceButtonsContainer.setVisibility(TextView.INVISIBLE);

        Map<String, String> params = new HashMap();
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_BOOKS_FOR_CHOICE, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno: ", response.toString());

                        try {
//                            listaLivros = response.getJSONArray("livros");
                            listaLivros = BookFeeder.booksFromJSONArray(response.getJSONArray("livros"));

                            if (listaLivros.size() <= 0) {
                                bookLoadInfo.setVisibility(TextView.GONE);
                                bookCoverContainer.setVisibility(ImageView.GONE);
                                noBooksInfo.setVisibility(TextView.VISIBLE);
                                choiceButtonsContainer.setVisibility(TextView.INVISIBLE);
                            } else {
                                bookLoadInfo.setVisibility(TextView.GONE);
                                bookCoverContainer.setVisibility(ImageView.VISIBLE);
                                noBooksInfo.setVisibility(TextView.GONE);
                                choiceButtonsContainer.setVisibility(TextView.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        carregando = false;

                        new VolleyCallback() {
                            @Override
                            public void onSuccess() {
                                nextBook();
                            }
                        }.onSuccess();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro: ", error.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(jsObjRequest);
    }

    public interface VolleyCallback {
        void onSuccess();

    }

    public void chooseBookYes(View view) {
        chooseBook(true);
    }

    public void chooseBookNo(View view) {
        chooseBook(false);
    }

    public void zerarEscolhasDev(View view) {

        Map<String, String> params = new HashMap();
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        Log.d("ZERAR > user_id:", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        JSONObject parameters = new JSONObject(params);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_CLEAN_CHOICES, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());

                        try {
                            JSONObject status = response.getJSONObject("status");
                            Boolean sucesso = status.getBoolean("success");
                            if (!sucesso) {
                                Toast toast = Toast.makeText(getApplicationContext(), "erro" + "Erro de comunicação com o servidor.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            loadBookListForChoice();

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


    private void chooseBook(Boolean choice) {

        carregando = true;
        bookLoadInfo.setVisibility(TextView.VISIBLE);
        bookCoverContainer.setVisibility(ImageView.GONE);
        noBooksInfo.setVisibility(TextView.GONE);
        choiceButtonsContainer.setVisibility(TextView.INVISIBLE);

        Map<String, String> params = new HashMap();
        params.put("fbook_id", livroAtual.getPhysicalBookId().toString());
        params.put("user_id", PrefUtils.getLoggedUser(getApplicationContext()).userId);
        params.put("matched", choice ? "1" : "0");
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_COMMIT_CHOICE, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Retorno: ", response.toString());

                        try {
                            JSONObject status = response.getJSONObject("status");
                            Boolean sucesso = status.getBoolean("success");
                            if (!sucesso) {
                                Toast toast = Toast.makeText(getApplicationContext(), "erro" + "Erro de comunicação com o servidor.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            nextBook();

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

    public void nextBook() {

        if (listaLivros.size() <= 0) return;

        if (bookListIndex < listaLivros.size() - 1) {
            ++bookListIndex;
        } else {
            loadBookListForChoice();
            return;
        }

        livroAtual = listaLivros.get(bookListIndex);

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(AppUtils.APP_PATH_VIRTUAL_BOOK_COVER_PATH + livroAtual.getPhoto(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        bookCover.setImageBitmap(bitmap);

                        bookLoadInfo.setVisibility(TextView.GONE);
                        bookCoverContainer.setVisibility(ImageView.VISIBLE);
                        noBooksInfo.setVisibility(TextView.GONE);
                        choiceButtonsContainer.setVisibility(TextView.VISIBLE);

                        carregando = false;
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //bookCover.setImageResource(R.drawable.image_load_error);
                    }
                });
// Access the RequestQueue through your singleton class.
        Volley.newRequestQueue(this).add(request);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void goToMyProfile() {

        Intent intent = new Intent(MainActivity.this, MyProfile.class);
        startActivity(intent);
    }

    public void goToMyBookGallery() {

        PrefUtils.setTargerUser(PrefUtils.getLoggedUser(getApplicationContext()), getApplicationContext());

        Intent intent = new Intent(MainActivity.this, BookGalleryActivity.class);
        startActivity(intent);
    }

    public void goToBookAdd() {
        Intent intent = new Intent(MainActivity.this, BookAddActivity.class);
        startActivity(intent);
    }

    public void goToBookTransactions() {
        Intent intent = new Intent(MainActivity.this, BookTransactionActivity.class);
        startActivity(intent);
    }

    public void goToSettings() {

    }

    public void goToBookDetail(View view) {

        PrefUtils.setCurrentBook(livroAtual, getApplicationContext());

        AppUser u = new AppUser();
        u.userId = livroAtual.getOwnerUserId();

        PrefUtils.setTargerUser(u, getApplicationContext());

        Intent intent = new Intent(MainActivity.this, BookAndProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_profile:
                // About option clicked.
                goToMyProfile();
                return true;
            case R.id.action_my_book_gallery:
                // About option clicked.
                goToMyBookGallery();
                return true;
            case R.id.action_book_add:
                // About option clicked.
//                goToSettings();
                goToBookAdd();
                return true;
//            case R.id.action_book_transactions:
//                // About option clicked.
////                goToSettings();
//                goToBookTransactions();
//                return true;
//            case R.id.action_settings:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

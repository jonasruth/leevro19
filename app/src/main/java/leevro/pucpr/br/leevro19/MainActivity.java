package leevro.pucpr.br.leevro19;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class MainActivity extends ActionBarActivity {

    private TextView bookLoadInfo;
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
        params.put("user_id", "1");
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
        params.put("user_id", PrefUtils.getCurrentUser(getApplicationContext()).userId);
        Log.d("ZERAR > user_id:",PrefUtils.getCurrentUser(getApplicationContext()).userId);
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
        params.put("user_id", "1");
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
//            myLocation = new Location("");
//            myLocation.setLatitude(-25.398847);
//            myLocation.setLongitude(-49.281793);
        // rua ebenezer, 270, curitiba
        // -25.398847, -49.281793

        Location bookLocation = new Location("");
        bookLocation.setLatitude(-25.410937);
        bookLocation.setLongitude(-49.272639);
        // Rua Carlos Pioli, 133, Curitiba
        // -25.410937, -49.272639


        if (myLocation != null) {
            Float distance = myLocation.distanceTo(bookLocation);
//              Toast toast = Toast.makeText(getApplicationContext(), "distancia " + distance, Toast.LENGTH_LONG);
//              toast.show();

            Log.d("lat/lng:", myLocation.getLatitude() + "/" + myLocation.getLongitude());

            int distanceInt = Math.round(distance / 1000);

            txtDistance.setText("Está a " + distanceInt + "km de você");
        } else {
            txtDistance.setText("");
        }

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

    public void goToMyProfile() {
        Intent intent = new Intent(MainActivity.this, PublicProfileActivity.class);
        intent.putExtra("p_user_id", "1");
//        intent.putExtra("camefrom", "MainActivity");
        startActivity(intent);
    }

    public void goToLogout() {
        Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
        startActivity(intent);
    }

    public void goToBookDetail(View view) {

        PrefUtils.setCurrentBook(livroAtual, getApplicationContext());

        AppUser u = new AppUser();
        u.userId = livroAtual.getOwnerUserId();

        PrefUtils.setCurrentPublicProfile(u, getApplicationContext());

        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
//        intent.putExtra("p_fbook_id", livroAtual.getPhysicalBookId());
//        intent.putExtra("p_user_id", livroAtual.getOwnerUserId());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), PrefUtils.getCurrentUser(this).email, Toast.LENGTH_SHORT).show();

        bookLoadInfo = (TextView) findViewById(R.id.bookLoadInfo);
        noBooksInfo = (LinearLayout) findViewById(R.id.noBooksInfo);
        bookCoverContainer = (LinearLayout) findViewById(R.id.bookCoverContainer);
        bookCover = (ImageView) findViewById(R.id.bookCover);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        choiceButtonsContainer = (TableLayout) findViewById(R.id.choiceButtonsContainer);

        bookLoadInfo.setVisibility(TextView.GONE);
        bookCoverContainer.setVisibility(ImageView.GONE);
        noBooksInfo.setVisibility(TextView.GONE);
        choiceButtonsContainer.setVisibility(TextView.INVISIBLE);

        myLocation = new Location("");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                Toast.makeText(getApplicationContext(), "Status alterado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(String arg0) {
                Toast.makeText(getApplicationContext(), "Provider Habilitado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String arg0) {
                Toast.makeText(getApplicationContext(), "Provider Desabilitado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationChanged(Location location) {

                myLocation = location;
//                myLocation.setLatitude(location.getLatitude());
//                myLocation.setLongitude(location.getLongitude());
//                myLocation.setAccuracy(location.getAccuracy());
//                location
                Toast.makeText(getApplicationContext(), myLocation.getLatitude() + "/" + myLocation.getLongitude() + "/precisao:" + myLocation.getAccuracy() + "m", Toast.LENGTH_LONG).show();

//                TextView latitude = (TextView) findViewById( R.id.latitude);
//                TextView longitude = (TextView) findViewById( R.id.longitude);
//                TextView time = (TextView) findViewById( R.id.time);
//                TextView acuracy = (TextView) findViewById( R.id.Acuracy);
//                TextView provider = (TextView) findViewById( R.id.provider);
//
//                if( location != null ){
//                    latitude.setText( "Latitude: "+location.getLatitude() );
//                    longitude.setText( "Longitude: "+location.getLongitude() );
//                    acuracy.setText( "Precisão: "+location.getAccuracy()+"" );
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                    time.setText( "Data:"+sdf.format( location.getTime() ) );
//
//                    provider.setText( location.getProvider());
//                }

            }
        }, null);

        loadBookListForChoice();
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
            case R.id.action_my_book_gallery:
                // About option clicked.
                goToMyProfile();
                return true;
            case R.id.action_logout:
                // About option clicked.
                goToLogout();
                return true;
            case R.id.action_settings:
                // Settings option clicked.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }
}

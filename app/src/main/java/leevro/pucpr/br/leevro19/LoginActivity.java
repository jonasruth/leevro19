package leevro.pucpr.br.leevro19;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class LoginActivity extends Activity {
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    AppUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();

        setContentView(R.layout.activity_login);

        if (PrefUtils.getLoggedUser(LoginActivity.this) != null) {

            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);

            startActivity(homeIntent);

            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email", "user_friends", "user_birthday", "user_location");

        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                Log.d("xxxxx", object.toString());
                                user = new AppUser();
                                //user.userId = "1";
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.firstName = object.getString("first_name").toString();
                                user.lastName = object.getString("last_name").toString();
                                user.birthday = object.getString("birthday").toString();
                                user.gender = object.getString("gender").toString();
                                user.link = object.getString("link").toString();

                                Log.d("facebook: ", object.toString());

                                if (object.has("location") && object.getJSONObject("location").has("id")) {
                                    user.locationId = object.getJSONObject("location").getString("id").toString();
                                }
                                if (object.has("location") && object.getJSONObject("location").has("name")) {
                                    user.locationName = object.getJSONObject("location").getString("name").toString();
                                }
                                user.locale = object.getString("locale");
                                user.timezone = object.getString("timezone");
                                user.updatedTime = object.getString("updated_time").toString();
                                user.verified = object.getString("verified");

                                PrefUtils.setLoggedUser(user, LoginActivity.this);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            salvarDadosUsuario();

                            Toast.makeText(LoginActivity.this, "Seja bem-vindo " + user.name, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,birthday,first_name,last_name,name,email,gender,link,location,locale,timezone,updated_time,verified");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };

    private void salvarDadosUsuario() {
        Log.d("user.facebookID", user.facebookID);

        Map<String, String> params = new HashMap();
        params.put("user_id", user.userId);
        params.put("user_fb_facebook_id", user.facebookID);
        params.put("user_fb_email", user.email);
        params.put("user_fb_name",user.name);
        params.put("user_fb_first_name",user.firstName);
        params.put("user_fb_last_name",user.lastName);
        params.put("user_fb_birthday",user.birthday);
        params.put("user_fb_gender",user.gender);
        params.put("user_fb_link",user.link);
        params.put("user_fb_location_id",user.locationId);
        params.put("user_fb_location_name",user.locationName);
        params.put("user_fb_locale",user.locale);
        params.put("user_fb_timezone",user.timezone);
        params.put("user_fb_updated_time",user.updatedTime);
        params.put("user_fb_verified",user.verified);

        JSONObject parameters = new JSONObject(params);

        Log.d("SALVAR:",parameters.toString());


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppUtils.APP_URL_WS_KEEP_USER, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno salvar dados: ", response.toString());

                        try {
                            user.userId = response.getJSONObject("user").getString("id");
                            Log.d("USER ID <<<< ", user.userId);
                            PrefUtils.setLoggedUser(user,getApplicationContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro salvar dados: ", error.toString());
//                        Toast toast = Toast.makeText(getApplicationContext(), "erro" + error.toString(), Toast.LENGTH_SHORT);
//                        toast.show();

                    }
                });
        Volley.newRequestQueue(this).add(jsObjRequest);
    }

}
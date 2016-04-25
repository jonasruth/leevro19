package leevro.pucpr.br.leevro19;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.utils.PrefUtils;


public class LogoutActivity extends ActionBarActivity {

    private TextView btnLogout;
    private AppUser user;
    private ImageView profileImage;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        user= PrefUtils.getLoggedUser(LogoutActivity.this);
        profileImage= (ImageView) findViewById(R.id.profileImage);

        // fetching facebook's profile picture
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {
                    //imageURL = new URL("https://graph.facebook.com/" + "1435767690067161" + "/picture?type=large");
                    imageURL = new URL("http://96.126.115.143/leevrows/user_img/" + PrefUtils.getLoggedUser(getApplicationContext()).facebookID.toString()+".jpg");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                profileImage.setImageBitmap(bitmap);
            }
        }.execute();


        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.clearLoggedUser(LogoutActivity.this);


                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();


                Intent i= new Intent(LogoutActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void cancelarLogout(View view){
        Intent intent = new Intent(LogoutActivity.this, MyProfile.class);
        startActivity(intent);
    }
}
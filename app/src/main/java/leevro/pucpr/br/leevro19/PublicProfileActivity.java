package leevro.pucpr.br.leevro19;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class PublicProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefUtils.setTargerUser( PrefUtils.getLoggedUser(getApplicationContext()), getApplicationContext() );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);


    }

}

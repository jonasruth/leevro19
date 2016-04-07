package leevro.pucpr.br.leevro19;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class FriendsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class BookDetailActivity extends ActionBarActivity {

    public void goToPublicProfile(View view)
    {
        Intent intent = new Intent(BookDetailActivity.this, PublicProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

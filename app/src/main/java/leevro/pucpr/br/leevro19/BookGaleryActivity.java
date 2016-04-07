package leevro.pucpr.br.leevro19;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class BookGaleryActivity extends ActionBarActivity {

    public void goToBookPropose(View view)
    {
        Intent intent = new Intent(BookGaleryActivity.this, BookAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_galery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

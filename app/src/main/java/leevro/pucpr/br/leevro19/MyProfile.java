package leevro.pucpr.br.leevro19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.network.CustomVolleyRequestQueue;
import leevro.pucpr.br.leevro19.utils.AppUtils;
import leevro.pucpr.br.leevro19.utils.PrefUtils;

public class MyProfile extends ActionBarActivity {
    private AppUser loggedUser;
    private ImageLoader mImageLoader;
    String endereco = null;
    LinearLayout addressViewContainer;
    LinearLayout addressEditContainer;
    TextView addressView;
    EditText addressEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loggedUser = PrefUtils.getLoggedUser(getApplicationContext());
        preencherCamposFragment();
    }

    private void preencherCamposFragment() {
        Context ctx = getApplicationContext();
        NetworkImageView profileImage = (NetworkImageView) findViewById(R.id.profileImage);
        TextView profileName = (TextView) findViewById(R.id.txtUserName);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getImageLoader();
        profileImage.setImageUrl(AppUtils.APP_PATH_USER_PIC_PATH + loggedUser.facebookID + ".jpg", mImageLoader);
        profileName.setText(loggedUser.name);
        addressViewContainer = (LinearLayout) findViewById(R.id.addressViewContainer);
        addressView = (TextView) findViewById(R.id.addressView);
        addressEditContainer = (LinearLayout) findViewById(R.id.addressEditContainer);
        addressEdit = (EditText) findViewById(R.id.addressEdit);

        if (endereco!=null && !endereco.isEmpty() ) {
            viewMode();
        }else{
            editMode();
        }

    }

    public void editMode(){
        addressEdit.setText(endereco);
        addressViewContainer.setVisibility(LinearLayout.GONE);
        addressEditContainer.setVisibility(LinearLayout.VISIBLE);
    }

    public void viewMode(){
        addressViewContainer.setVisibility(LinearLayout.VISIBLE);
        addressEditContainer.setVisibility(LinearLayout.GONE);
    }

    public void saveAddress(){
        endereco =  addressEdit.getText().toString();
        addressView.setText(endereco);
        addressEdit.setText(null);
        Toast.makeText(getApplicationContext(),"Endereço salvo!",Toast.LENGTH_SHORT).show();
    }

    public void onclickBtnEdit(View view){
        editMode();
    }

    public void onclickBtnSave(View view){
        AppUtils.esconderTecladoVirtual(this);
        saveAddress();
        viewMode();
    }

    public void goToLogout(View view) {
        Intent intent = new Intent(MyProfile.this, LogoutActivity.class);
        startActivity(intent);
    }

}

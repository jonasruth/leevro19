package leevro.pucpr.br.leevro19;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class PublicProfileActivityFragment extends Fragment {

    public PublicProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_public_profile, container, false);

//        Log.d("xxxx",this.getArguments().toString());

        // Inicio do meu código

        Bundle extras = getActivity().getIntent().getExtras();
        String p_fbook_id = null;
        String p_user_id = null;

        if (extras != null) {
            p_fbook_id = extras.getString("p_fbook_id");
            p_user_id = extras.getString("p_user_id");
        }

//        TextView txtUserName = (TextView) view.findViewById(R.id.txtUserName);
//        txtUserName.setText("xxxxxx = " + p_user_id);
        // Fim do meu código

        return view;
    }
}

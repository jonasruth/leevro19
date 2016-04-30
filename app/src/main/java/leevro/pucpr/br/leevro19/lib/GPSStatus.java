package leevro.pucpr.br.leevro19.lib;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jonas on 29/04/2016.
 */
public class GPSStatus implements Parcelable {

    public Location lastLocation;
    public boolean providerEnabled;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

package org.jwa.demoandroidintentrce;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;


/**
 * This is a parcelable that is vulnerable to RCE. In the real world a parcelable like this is
 * obviously a very bad idea; never use this class it is for demonstration purposes only!!!
 */
public class VulnerableParcelable implements Parcelable {

    private static final String TAG = "JWA/" + VulnerableParcelable.class.getSimpleName();

    private String value;

    public VulnerableParcelable() {
        Log.i(TAG, "Created empty VulnerableParcelable");
    }

    private VulnerableParcelable(Parcel source) {
        this.value = source.readString();

        Log.w(TAG, "Executing command " + value);

        // Vulnerability here, we execute the incoming String as a command, this is a bad idea
        ShellUtils.CommandResult result = ShellUtils.execCommand(value);

        if (result.result == 0) {
            Log.w(TAG, "Attack succeeded, command was executed", new Exception("trace"));
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(value);
    }

    public static final Parcelable.Creator<VulnerableParcelable> CREATOR = new Parcelable.Creator<VulnerableParcelable>() {
        public VulnerableParcelable createFromParcel(Parcel source) {
            return new VulnerableParcelable(source);
        }

        public VulnerableParcelable[] newArray(int size) {
            return new VulnerableParcelable[size];
        }
    };
}

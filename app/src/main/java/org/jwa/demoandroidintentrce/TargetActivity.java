package org.jwa.demoandroidintentrce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


/**
 * This is the victim target activity that an attacker hopes to use as a vector for an RCE
 * vulnerability via the VulnerableParcelable.
 */
public class TargetActivity extends AppCompatActivity {

    private static final String TAG = "JWA/" + TargetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        // Observe that in Android 13 calls to getStringExtra do not trigger a complete unparcel,
        // due to this change by Google:
        //
        // https://cs.android.com/android/_/android/platform/frameworks/base/+/9ca6a5e21a1987fd3800a899c1384b22d23b6dee
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String aString = getIntent().getStringExtra("aString");
            Log.i(TAG, "Incoming intent has string extra: " + aString);
        }

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(v -> {
            Log.i(TAG, "Calling finish");
            // Observe that in Android 12 and earlier this call to finish will trigger a complete
            // unparcel, it is therefore impossible to prevent an unparcel of all extras in Android
            // 12 and earlier.
            //
            // In Android 13 this does not trigger a complete unparcel.
            finish();
        });
    }
}
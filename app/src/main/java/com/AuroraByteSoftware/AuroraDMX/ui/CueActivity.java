package com.AuroraByteSoftware.AuroraDMX.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.AuroraByteSoftware.AuroraDMX.AuroraNetwork;
import com.AuroraByteSoftware.AuroraDMX.R;

/**
 * Display a full screen grid of cues
 */
public class CueActivity extends Activity {
    public GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cue);

        gridView = findViewById(R.id.cue_grid);
        gridView.setAdapter(new CueGridCell(this));

        AuroraNetwork.setUpNetwork(this);
    }

    @Override
    protected void onPause() {
        AuroraNetwork.stopNetwork();
        super.onPause();
    }

}

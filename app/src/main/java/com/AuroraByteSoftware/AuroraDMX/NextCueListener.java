package com.AuroraByteSoftware.AuroraDMX;

import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Listen to click events on the cue list
 * Created by furtchet on 12/6/15.
 */
class NextCueListener implements View.OnClickListener, View.OnLongClickListener {
    private Button button;

    /**
     * Cue Click handler
     */
    @Override
    public void onClick(View arg0) {
        if (arg0 instanceof Button) {
            button = (Button) arg0;
        }
        if (button != null && button.getContext() == null) {
            Log.e(getClass().getSimpleName(), "Cue button onclick had a null context");
            return;
        }

        int nextCue;
        if (MainActivity.getCueFade() != null && MainActivity.getCueFade().getUpwardCue() >= 0) {
            nextCue = MainActivity.getCueFade().getUpwardCue();
        } else if (!MainActivity.getAlCues().isEmpty()) {
            nextCue = -1;
        } else {
            return;
        }
        //if there is a next cue
        if (MainActivity.getAlCues().size() > nextCue + 1) {
            MainActivity.getCueFade().startCueFade(MainActivity.getAlCues().get(nextCue + 1));
        }
    }

    /**
     * Cue Long Click handler
     */
    @Override
    public boolean onLongClick(View buttonView) {
        return true;
    }
}

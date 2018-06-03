package com.AuroraByteSoftware.AuroraDMX;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.AuroraByteSoftware.AuroraDMX.ui.EditCueMenu;

/**
 * Listen to click events on the cue list
 * Created by furtchet on 12/6/15.
 */
public class CueSheetClickListener implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private Button button;

    /**
     * Cue Click handler
     */
    @Override
    public void onClick(View arg0) {
        if (arg0 instanceof Button) {
            button = (Button) arg0;
        }
        if (button == null || button.getContext() == null) {
            Log.e(getClass().getSimpleName(), "Cue button onclick had a null context");
            return;
        }
        context = button.getContext();
        int curCue = -1;// Current cue number on alCues scale
        for (int x = 0; x < MainActivity.alCues.size(); x++) {
            if (button == MainActivity.alCues.get(x).getButton()) {
                curCue = x;
                break;
            }
        }

        if (curCue == -1) {// Adding a new cue
            return;
        } else {
            // ======= Loading a cue ========
            Log.d(getClass().getSimpleName(), "oldChLevels " + MainActivity.getCurrentChannelArray());
            MainActivity.getCueFade().startCueFade(MainActivity.alCues.get(curCue));
        }
    }

    /**
     * Cue Long Click handler
     */
    @Override
    public boolean onLongClick(View buttonView) {
        boolean buttonIsAddCue = true;
        for (CueObj cue : MainActivity.alCues) {
            if (cue.getButton() == buttonView) {
                buttonIsAddCue = false;
            }
        }
        if (!buttonIsAddCue) {
            EditCueMenu.createEditCueMenu(MainActivity.alCues, (Button) buttonView, true);
        }
        return true;
    }
}

package com.AuroraByteSoftware.AuroraDMX.test;


import android.widget.LinearLayout;

import com.AuroraByteSoftware.AuroraDMX.MainActivity;
import com.AuroraByteSoftware.AuroraDMX.fixture.Fixture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DummyFixture extends Fixture {


    private int ChNum = 0;

    private double step[] = new double[3];
    private double stepIteram[] = new double[3];
    private String chText = "";
    private List<Integer> rgbLevel = new ArrayList<>(Collections.nCopies(3, 0));

    public DummyFixture(final MainActivity context, String channelName) {
        this.chText = channelName == null ? this.chText : channelName;
        init();
    }

    public DummyFixture(final MainActivity context, String channelName, LinearLayout viewGroup) {
        this.chText = channelName == null ? this.chText : channelName;

    }

    @Override
    public void init() {
    }


    /**
     * Get the LinearLayout that contains the text, slider, and button for one
     * channel
     *
     * @return the viewGroup
     */
    @Override
    public LinearLayout getViewGroup() {
        return null;
    }


    /**
     * Sets the level of the channel
     *
     * @param a_chLevel set the level
     */
    @Override
    public void setChLevels(List<Integer> a_chLevel) {
        for (int i = 0; i < a_chLevel.size(); i++) {
            rgbLevel.set(i, Math.min(MAX_LEVEL, a_chLevel.get(i)));
        }
        updateFixtureLevelText();
    }

    @Override
    protected void updateFixtureLevelText() {
    }

    /**
     * get the level of the channel
     */
    @Override
    public List<Integer> getChLevels() {
        return rgbLevel;
    }

    @Override
    public String toString() {
        return ("Ch: " + ChNum + "\tLvl: " + rgbLevel);
    }

    /**
     * Creates 255 steeps between current and endVal
     *
     * @param endVal
     */
    @Override
    public void setupIncrementLevelFade(List<Integer> endVal) {
        step[0] = (endVal.get(0) - rgbLevel.get(0)) / 256.0;
        step[1] = (endVal.get(1) - rgbLevel.get(1)) / 256.0;
        step[2] = (endVal.get(2) - rgbLevel.get(2)) / 256.0;
        stepIteram[0] = rgbLevel.get(0);
        stepIteram[1] = rgbLevel.get(1);
        stepIteram[2] = rgbLevel.get(2);
    }

    /**
     * Adds one step Up to the current level
     */
    @Override
    public void incrementLevelUp() {
        if (step[0] > 0)
            stepIteram[0] += step[0];
        if (step[1] > 0)
            stepIteram[1] += step[1];
        if (step[2] > 0)
            stepIteram[2] += step[2];
        updateIncrementedLevel();
    }

    /**
     * Adds one step Down to the current level
     */
    @Override
    public void incrementLevelDown() {
        if (step[0] < 0)
            stepIteram[0] += step[0];
        if (step[1] < 0)
            stepIteram[1] += step[1];
        if (step[2] < 0)
            stepIteram[2] += step[2];
        updateIncrementedLevel();
    }

    private void updateIncrementedLevel() {
        rgbLevel.set(0, (int) stepIteram[0]);
        rgbLevel.set(1, (int) stepIteram[1]);
        rgbLevel.set(2, (int) stepIteram[2]);
    }

    @Override
    public void setScrollColor(int scrollColor) {
        //RGB has its own color
    }

    public void setColumnText(String text) {
        this.chText = text;
    }

    @Override
    public String getChText() {
        return chText;
    }

    @Override
    public boolean isRGB() {
        return true;
    }

    @Override
    public void removeSelector() {

    }

    @Override
    public void setFixtureNumber(int currentFixtureNum) {
    }

    public void setChLevelArray(List<Integer> chLevels) {
        rgbLevel = chLevels;
    }
}

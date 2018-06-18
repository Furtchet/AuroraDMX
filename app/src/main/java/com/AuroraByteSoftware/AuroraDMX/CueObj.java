package com.AuroraByteSoftware.AuroraDMX;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class CueObj implements Serializable {
    /**
     * s
     */
    private static final long serialVersionUID = -6835651116806619514L;
    private int fadeUpTime = 5;
    private int fadeDownTime = 5;
    private ArrayList<Integer> levelsList;
    private int[] levels = new int[0];
    private transient Button button;
    private int highlight = 0;
    private boolean fadeInProgress = false;
    private String name = "";
    private transient Timer fadeTimer = null;
    private transient int r = 0;
    private transient int g = 0;
    private transient int b = 0;

    /**
     * makes android compiler happier
     */
    public CueObj() {
    }

    /**
     * @return the button
     */
    public Button getButton() {
        return button;
    }

    /**
     * @param button the button to set
     */
    public void setButton(Button button) {
        this.button = button;
    }

    public CueObj(String cueName, int a_fadeUpTime, int a_fadeDownTime, List<Integer> a_levels, Button a_button) {
        fadeUpTime = a_fadeUpTime;
        fadeDownTime = a_fadeDownTime;
        levelsList = new ArrayList<>(a_levels);
        button = a_button;
        name = cueName;
    }

    public CueObj(String cueName, int a_fadeTime, List<Integer> a_levels, Button a_button) {
        this(cueName, a_fadeTime, a_fadeTime, a_levels, a_button);
    }

    /**
     * @return the fadeUpTime
     */
    public int getFadeUpTime() {
        return fadeUpTime;
    }

    /**
     * @param fadeUpTime2 the fadeUpTime to set
     */
    public void setFadeUpTime(int fadeUpTime2) {
        this.fadeUpTime = fadeUpTime2;
    }

    /**
     * @return the fadeDownTime
     */
    public int getFadeDownTime() {
        return fadeDownTime;
    }

    /**
     * @param fadeDownTime2 the fadeDownTime to set
     */
    public void setFadeDownTime(int fadeDownTime2) {
        this.fadeDownTime = fadeDownTime2;
    }

    /**
     * @return the levels
     */
    public ArrayList<Integer> getLevels() {
        return levelsList;
    }

    /**
     * Sets the cue button to be highlighted with its progress
     */
    public void setHighlight(int r, int g, int b) {
        highlight = r + g + b;
        this.r = r;
        this.g = g;
        this.b = b;
        if (highlight != 0) {
            button.getBackground().setColorFilter(Color.argb(255, r, g, b), Mode.DARKEN);
            button.postInvalidate();
        } else {
            button.getBackground().setColorFilter(null);
        }
    }

    public void refreshHighlight() {
        if (highlight != 0) {
            button.getBackground().setColorFilter(Color.argb(255, r, g, b), Mode.DARKEN);
            button.postInvalidate();
        } else {
            button.getBackground().setColorFilter(null);
        }
    }

    public int getHighlight() {
        return highlight;
    }

    /**
     * @return the fadeInProgress
     */
    public boolean isFadeInProgress() {
        return fadeInProgress;
    }

    /**
     * @param fadeInProgress the fadeInProgress to set
     */
    public void setFadeInProgress(boolean fadeInProgress) {
        this.fadeInProgress = fadeInProgress;
    }

    public void padChannels(int number_channels) {
        if (number_channels > levelsList.size()) {
            for (int i = levelsList.size(); i < number_channels; i++) {
                levelsList.add(0);
            }
        } else {
            levelsList = new ArrayList<>(levelsList.subList(0, number_channels));
        }
    }

    public String getCueName() {
        return name;
    }

    public void setCueName(String name) {
        this.name = name;
    }

    public int[] getOriginalLevels() {
        return levels;
    }

    public void setOriginalLevels(int[] array) {
        levels = array;
    }

    public void setLevelsList(List<Integer> levelsList) {
        this.levelsList = new ArrayList<>(levelsList);
    }

    public Timer getFadeTimer() {
        return fadeTimer;
    }

    public void setFadeTimer(Timer fadeTimer) {
        this.fadeTimer = fadeTimer;
    }
}

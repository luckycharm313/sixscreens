package com.tigerphp.sixscreensdemo.sixscreensdemo.models;

/**
 * Created by luckycharm on 7/8/18.
 */

public class Pusher {
    private String action;
    private String photoURL;
    private String jobName;
    private String breakType;
    private boolean isClockIn;

    public Pusher(String action) {
        this.action = action;
    }

    public Pusher(String action, String photoUrl, String jobName, Boolean isIn) {
        this.action = action;
        this.photoURL= photoUrl;
        this.jobName= jobName;
        this.isClockIn = isIn;
    }

    public Pusher(String action, String jobName, Boolean isIn) {
        this.action = action;
        this.jobName= jobName;
        this.isClockIn = isIn;
    }

    public Pusher(String action, String breakType) {
        this.action = action;
        this.breakType = breakType;
    }
    public Pusher(String action, String photoUrl, String breakType) {
        this.action = action;
        this.breakType= breakType;
        this.photoURL= photoUrl;
    }
    public boolean isClockIn() {
        return isClockIn;
    }

    public void setClockIn(boolean clockIn) {
        isClockIn = clockIn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    public String getBreakType() {
        return breakType;
    }

    public void setBreakType(String breakType) {
        this.breakType = breakType;
    }
}

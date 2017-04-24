package com.example.dell.actplus;

import android.graphics.Bitmap;

/**
 * Created by DELL on 2017/4/19.
 */

public class ActItem {
    private Bitmap image;
    private String ActName, ActTime, ActId, ActPlace;
    public ActItem(String actName, String actTime, String actId, String actPlace) {
        ActName = actName;
        ActId = actId;
        ActTime = actTime;
        ActPlace = actPlace;
    }
    public void SetImage(Bitmap bitmap) {
        if (bitmap != null) {
            image = bitmap;
        }
    }
    public Bitmap getImage() {
        return image;
    }

}

package com.example.dell.actplus;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by DELL on 2017/4/19.
 */

public class NetTools {
    public void getListJson(int startPage, int pageSize, String pageType) {
        HttpURLConnection connection = null;
        String url = "http://actplus.sysuactivity.com/api/actlist";
        try {
            Log.i("getListJson", "start connecting");
            connection = (HttpURLConnection) ((new URL(url.toString())).openConnection());
            connection.setRequestMethod("GET");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            InputStream in = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            String response;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch(Exception e) {
            Log.i(TAG, "getListJson: " + e.toString());
        }
    }
    public List<ActItem> getList() {
        List<ActItem> data = new ArrayList<>();
        return data;
    }
    public Bitmap getImage(String imageType, String imageName) {
        Bitmap bitmap;
        return null;
    }
}

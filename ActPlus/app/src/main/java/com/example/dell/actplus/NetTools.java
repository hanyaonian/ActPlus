package com.example.dell.actplus;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

import static android.content.ContentValues.TAG;

/**
 * Created by DELL on 2017/4/19.
 */

public class NetTools {
    public String getListJson(int startPage, int pageSize, String pageType) {
        HttpURLConnection connection = null;
        String url = "http://actplus.sysuactivity.com/api/actlist";
        try {
            Log.i("getListJson", "start connecting");
            connection = (HttpURLConnection) ((new URL(url.toString())).openConnection());
            connection.setRequestMethod("GET");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            InputStream in = connection.getInputStream();
            //get json string
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line;
                String response = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                return response;
            }
            else {
                return null;
            }
        } catch(Exception e) {
            Log.i(TAG, "getListJson: " + e.toString());
        }
        return null;
    }
    public List<ActItem> getList(final int startPage,final int pageSize,final String pageType) {
        List<ActItem> data = new ArrayList<>();
        String shit = getListJson(startPage, pageSize, pageType);
        try {
            JSONArray jsonArray = new JSONArray(shit);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                //get detail
                ActItem item = new ActItem(temp.getString("actName"), temp.getString("actTime"), temp.getString("actId"),temp.getString("actLoc"));
                //get poster image
                item.SetImage(getImage("poster", temp.getString("posterName")));
                data.add(item);
            }
        } catch (Exception e) {
            Log.i("jsonArray part", "error");
        }
        return data;
    }
    public Bitmap getImage(String imageType, String imageName) {
        Bitmap bitmap;
        return null;
    }
}

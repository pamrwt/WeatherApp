package com.web114.weatherapp;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by user on 5/16/2018.
 */

public class CustomApplication extends Application {
    public InputStream getJsonStream(){
        AssetManager mgr = getAssets();
        String filename = null;
        InputStream stream = null;
        try {
            filename = "city.list.json";
            System.out.println("filename : " + filename);
            stream = mgr.open(filename, AssetManager.ACCESS_BUFFER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
    public List<ListJsonObject> readStream(InputStream stream) {
        JsonReader reader = null;
        List<ListJsonObject> messages = new ArrayList<ListJsonObject>();
        try {
            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            Gson gson = new GsonBuilder().create();
            reader.beginArray();
            while (reader.hasNext()) {
            //    ListJsonObject message = gson.fromJson(reader, ListJsonObject.class);
             //   messages.add(message);
            }
            reader.endArray();
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException ex) {
        }
        Collections.sort(messages, new Comparator<ListJsonObject>() {
            @Override
            public int compare(ListJsonObject listJsonObject, ListJsonObject nextListJsonObject) {
                return listJsonObject.getName().compareToIgnoreCase(nextListJsonObject.getName());
            }
        });
        return messages;
    }
}

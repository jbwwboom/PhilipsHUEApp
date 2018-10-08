package com.example.gebruiker.philipshueapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HueManager {

    private static HueManager instance = null;

    private Context context;

    private HueListener listener;

    private RequestQueue queue;

    private HueManager(Context context, HueListener listener) {
        this.context = context;
        this.listener = listener;
        queue = Volley.newRequestQueue(context);
    }

    // Singleton
    public static HueManager getInstance(Context context, HueListener listener) {
        if (instance == null) {
            instance = new HueManager(context, listener);
        }
        return instance;
    }

    public void volleyGet() {
        // Instantiate the RequestQueue.
        String url = Config.API_URL;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the JSON
                        try {
                            ArrayList<Light> lights = new ArrayList<>();

                            //NEW PARSES
                            Map<String, Object> test = jsonToMap(response);
                            for(String i : test.keySet()){
                            //OLD PARSER
                            //for (int i = 1; i <= response.length(); i++) {
                                // Get current json object
                                JSONObject jsonLight = response.getJSONObject(i);

                                String modelid = jsonLight.getString(Config.MODELID);
                                String name = jsonLight.getString(Config.NAME);
                                JSONObject state = jsonLight.getJSONObject(Config.STATE);
                                boolean on = state.getBoolean(Config.ON);
                                int hue = state.getInt(Config.HUE);
                                int sat = state.getInt(Config.SATURATION);
                                int bri = state.getInt(Config.BRIGHTNESS);

                                Light light = new Light(Integer.valueOf(i), modelid, name, on, hue, sat, bri);
                                lights.add(light);
                            }
                            listener.onResponse(lights);
                        } catch (Exception e) {
                            Log.d(e.getLocalizedMessage(), "Error");
                            listener.onError(new Error(e.getLocalizedMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(new Error(error.getLocalizedMessage()));
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void volleyPut(String url, final HashMap<String, Object> params) {
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        queue.add(putRequest);
    }

    public void switchOn(String url, boolean on) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Config.ON, on);
        this.volleyPut(url, params);
    }

    public void putHue(String url, int hue) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Config.HUE, hue);
        this.volleyPut(url, params);
    }

    public void putSaturation(String url, int saturation) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Config.SATURATION, saturation);
        this.volleyPut(url, params);
    }

    public void putBrightness(String url, int brightness) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Config.BRIGHTNESS, brightness);
        this.volleyPut(url, params);
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}

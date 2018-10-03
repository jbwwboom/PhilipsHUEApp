package com.example.gebruiker.philipshueapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
                            // Loop through the array elements
                            for (int i = 1; i <= response.length(); i++) {
                                // Get current json object
                                JSONObject jsonLight = response.getJSONObject("" + i);

                                String modelid = jsonLight.getString(Config.MODELID);
                                String name = jsonLight.getString(Config.NAME);
                                JSONObject state = jsonLight.getJSONObject(Config.STATE);
                                boolean on = state.getBoolean(Config.ON);
                                int hue = state.getInt(Config.HUE);
                                int sat = state.getInt(Config.SATURATION);
                                int bri = state.getInt(Config.BRIGHTNESS);

                                Light light = new Light(i, modelid, name, on, hue, sat, bri);
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

    public void volleyPut(String url, final HashMap<String, String> params) {
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
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Config.ON, String.valueOf(on));
        this.volleyPut(url, params);
    }

    public void putHue(String url, int hue) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Config.HUE, String.valueOf(hue));
        this.volleyPut(url, params);
    }

    public void putSaturation(String url, int saturation) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Config.SATURATION, String.valueOf(saturation));
        this.volleyPut(url, params);
    }

    public void putBrightness(String url, int brightness) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Config.BRIGHTNESS, String.valueOf(brightness));
        this.volleyPut(url, params);
    }

}

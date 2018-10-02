package com.example.gebruiker.philipshueapp;


import java.util.ArrayList;

public interface HueListener {

    void onResponse(ArrayList<Light> users);

    void onError(Error error);
}

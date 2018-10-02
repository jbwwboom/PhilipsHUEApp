package com.example.gebruiker.philipshueapp;

import java.io.Serializable;

public class Light implements Serializable {
    private int id;
    private String modelid;
    private String name;
    private boolean on;
    private int hue;
    private int saturation;
    private int brightness;

    public Light(int id, String modelid, String name, boolean on, int hue, int saturation, int brightness) {
        this.id = id;
        this.modelid = modelid;
        this.name = name;
        this.on = on;
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    @Override
    public String toString() {
        return "Light{" +
                "id=" + id +
                ", modelid='" + modelid + '\'' +
                ", name='" + name + '\'' +
                ", on=" + on +
                ", hue=" + hue +
                ", saturation=" + saturation +
                ", brightness=" + brightness +
                '}';
    }
}

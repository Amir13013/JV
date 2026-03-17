package com.eremon.models.entity;

public class Effect {
    private String name;
    private String type;
    private int value;
    private int duration;

    public Effect(String name, String type, int value, int duration) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.duration = duration;
    }

    public void tick() {
        duration--;
    }

    public boolean isExpired() {
        return duration <= 0;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getValue() { return value; }
    public int getDuration() { return duration; }
}

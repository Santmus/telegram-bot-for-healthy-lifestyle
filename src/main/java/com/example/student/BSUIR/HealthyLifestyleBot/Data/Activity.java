package com.example.student.BSUIR.HealthyLifestyleBot.Data;

public enum Activity {
    MIN_ACTIVITY(1.2f),
    LOW_ACTIVITY(1.375f),
    NORMAL_ACTIVITY(1.55f),
    HIGH_ACTIVITY(1.725f),
    EXTRA_ACTIVITY(1.9f);

    private float value;

    Activity(float value){
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}

package com.bgs.parking.model;

public enum Cathegorie {
    DEUX_ROUES(2),
    QUATRES_ROUES(4);
    private final int numberOfWheel;

    Cathegorie(int numberOfWheel) {
        this.numberOfWheel = numberOfWheel;
    }
}

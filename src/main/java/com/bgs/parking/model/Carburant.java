package com.bgs.parking.model;

import lombok.Getter;

@Getter
public enum Carburant {
    ESSENCE("essence"),
    GAZOIL("gazoil"),
    ELECTRIQUE("electrique");

    private final String nom;

    Carburant(String nom) {
        this.nom = nom;
    }
}

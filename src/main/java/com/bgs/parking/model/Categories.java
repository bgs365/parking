package com.bgs.parking.model;

import lombok.Getter;

@Getter
public enum Categories {
    DEUX_ROUES(2, "moto"),
    QUATRES_ROUES(4, "voiture");
    private final int nombreDeRoues;
    private final String nom;

    Categories(int nombreDeRoues, String nom) {
        this.nombreDeRoues = nombreDeRoues;
        this.nom = nom;
    }

}

package com.bgs.parking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Vehicule {
    private long id;
    private Categories categorie;
    private Carburant typeCarburant;
}

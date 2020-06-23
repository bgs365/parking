package com.bgs.parking.model;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Parking {
    private long id;
    private List<MouvementVehicule> mouvementVehicules;
    private Map<Categories,Double> tarif;
    private int nombreDePlaceDisponoible;
}

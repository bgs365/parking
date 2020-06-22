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
    private Set<Cathegorie> vehiculesAcceptee;
    private List<MouvementVehicules> mouvementVehicules;
    private Map<Cathegorie,Double> tarif;
    private int nombreDePlaceDisponoible;
}

package com.bgs.parking.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class MouvementVehicule {
    @NonNull
    private Vehicule vehicule;
    @NonNull
    private LocalDateTime heureEntree;
    private LocalDateTime heureSortie;
}

package com.bgs.parking.service;

import com.bgs.parking.model.MouvementVehicule;
import com.bgs.parking.model.Parking;
import com.bgs.parking.model.Vehicule;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ParkingGestionService {

    private static final double NOMBRE_DE_SECONDE_PAR_HEURE = 3600.;

    public boolean vehiculeEntrant(Parking parking, Vehicule vehicule, LocalDateTime heureEntree){
        return parking.getMouvementVehicules().add(
                MouvementVehicule.builder()
                        .vehicule(vehicule)
                        .heureEntree(heureEntree)
                        .build()
        );
    }

    public boolean vehiculeSortant(Parking parking, Vehicule vehicule, LocalDateTime heureSortie){
        AtomicBoolean estSortie = new AtomicBoolean(false);
        parking.getMouvementVehicules().stream()
                    .filter(
                            mouvementVehicule -> mouvementVehicule.getVehicule().equals(vehicule)
                                    && mouvementVehicule.getHeureSortie() == null
                                    && mouvementVehicule.getHeureEntree() !=null
                    )
                    .findFirst()
                    .ifPresent(
                            mouvementVehicules -> {
                                mouvementVehicules.setHeureSortie(heureSortie);
                                estSortie.set(true);
                            }
                    );
        return estSortie.get();
    }

    public Duration tempsPasseAuParkingLorsDuDernierPasssage(Parking parking, Vehicule vehicule){
        AtomicReference<Duration> tempsPasse = new AtomicReference<>();
        List<MouvementVehicule> mouvementList =  parking.getMouvementVehicules().stream().filter(
                mouvementVehicule -> mouvementVehicule.getVehicule().equals(vehicule)
        ).collect(Collectors.toList());
        long nombreDeMouvementPourLeVehicule = mouvementList.size();
        mouvementList.stream().skip(nombreDeMouvementPourLeVehicule - 1)
                .findFirst()
                .ifPresent(
                mouvementVehicules -> tempsPasse.set(
                        Duration.between(mouvementVehicules.getHeureEntree(), mouvementVehicules.getHeureSortie())
                )
        );

        return tempsPasse.get();
    }

    public Double montantDu(Parking parking, Vehicule vehicule){
        long nombreHeures = (long)Math.ceil(
                tempsPasseAuParkingLorsDuDernierPasssage(parking, vehicule).getSeconds() / NOMBRE_DE_SECONDE_PAR_HEURE
        );
        return nombreHeures * parking.getTarif().get(vehicule.getCathegorie());
    }


}

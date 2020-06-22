package com.bgs.parking.service;

import com.bgs.parking.model.MouvementVehicules;
import com.bgs.parking.model.Parking;
import com.bgs.parking.model.Vehicule;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ParkingGestionService {

    public boolean vehiculeEntrant(Parking parking, Vehicule vehicule, LocalDateTime heureEntree){
        return parking.getMouvementVehicules().add(
                MouvementVehicules.builder()
                        .vehicule(vehicule)
                        .heureEntree(heureEntree)
                        .build()
        );
    }

    public boolean vehiculeSortant(Parking parking, Vehicule vehicule, LocalDateTime heureSortie){
        try {
            parking.getMouvementVehicules().stream()
                    .filter(
                            mouvementVehicule -> mouvementVehicule.getVehicule().equals(vehicule)
                                    && mouvementVehicule.getHeureSortie() == null
                    )
                    .findFirst()
                    .ifPresent(
                            mouvementVehicules -> mouvementVehicules.setHeureSortie(heureSortie)
                    );
            return true;
        } catch ( Exception e){
            return false;
        }
    }

    public Duration tempsPasseAuParking(Parking parking, Vehicule vehicule){
        AtomicReference<Duration> tempsPasse = new AtomicReference<>();
        List<MouvementVehicules> mouvementList =  parking.getMouvementVehicules().stream().filter(
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
        long numberOfHours = (long)Math.ceil(tempsPasseAuParking(parking, vehicule).getSeconds() / 3600.);
        return numberOfHours * parking.getTarif().get(vehicule.getCathegorie());
    }


}

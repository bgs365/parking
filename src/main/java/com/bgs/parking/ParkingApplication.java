package com.bgs.parking;

import com.bgs.parking.model.Carburant;
import com.bgs.parking.model.Categories;
import com.bgs.parking.model.Parking;
import com.bgs.parking.model.Vehicule;
import com.bgs.parking.service.ParkingGestionService;
import com.bgs.parking.utils.FormateurDeDuree;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@SpringBootApplication
@Log4j2
public class ParkingApplication implements CommandLineRunner {

    private ParkingGestionService parkingGestionService;

    public ParkingApplication(ParkingGestionService parkingGestionService) {
        this.parkingGestionService = parkingGestionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        initParking();
    }

    private void initParking(){

        Map<Categories,Double> tarifs = new HashMap<Categories, Double>() {{
            put(Categories.QUATRES_ROUES,2.);
            put(Categories.DEUX_ROUES, 1.);
        }};

        Parking parking = Parking.builder()
                .id(1l)
                .tarif(tarifs)
                .nombreDePlaceDisponoible(50)
                .mouvementVehicules(new ArrayList<>())
                .build();

        Vehicule voiture = Vehicule.builder()
                .id(1l)
                .typeCarburant(Carburant.ESSENCE)
                .categorie(Categories.QUATRES_ROUES)
                .build();
        Vehicule moto = Vehicule.builder()
                .id(2l)
                .typeCarburant(Carburant.ESSENCE)
                .categorie(Categories.DEUX_ROUES)
                .build();
        parkingGestionService.estVehiculeEntrant(
                parking,
                voiture,
                LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(13,24))
        );
        parkingGestionService.estVehiculeEntrant(
                parking,
                moto,
                LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(19,30))
        );

        parkingGestionService.estVehiculeSortant(
                parking,
                voiture,
                LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(15,10))
        );
        parkingGestionService.estVehiculeSortant(
                parking,
                moto,
                LocalDateTime.of(LocalDate.of(2020,6,23), LocalTime.of(0,37))
        );

        log.info("-------------- VOITURE --------------");
        log.info(String.format("- véhicule : %s %s",
                voiture.getCategorie().getNom(),
                voiture.getTypeCarburant().getNom()
        ));
        log.info(String.format("- temps passé : %s",
                FormateurDeDuree.INSTANCE.formatDuration(parkingGestionService.tempsPasseAuParkingLorsDuDernierPasssage(parking, voiture))
        ));
        log.info(String.format("- montant dû : %.2f euros", parkingGestionService.montantDu(parking, voiture)));

        log.info("-------------- MOTO --------------");
        log.info(String.format("- véhicule : %s %s",
                moto.getCategorie().getNom(),
                moto.getTypeCarburant().getNom()
        ));
        log.info(String.format("- temps passé : %s",
                FormateurDeDuree.INSTANCE.formatDuration(parkingGestionService.tempsPasseAuParkingLorsDuDernierPasssage(parking, moto))
        ));
        log.info(String.format("- montant dû : %.2f euros", parkingGestionService.montantDu(parking, moto)));

        log.info(parking.toString());

    }

}

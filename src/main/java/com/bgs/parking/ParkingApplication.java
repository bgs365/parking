package com.bgs.parking;

import com.bgs.parking.model.Carburant;
import com.bgs.parking.model.Cathegorie;
import com.bgs.parking.model.Parking;
import com.bgs.parking.model.Vehicule;
import com.bgs.parking.service.ParkingGestionService;
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
        log.info("EXECUTING : command line runner");
        initParking();
    }

    private void initParking(){

        Set<Cathegorie> cathegoriesAcceptés = new HashSet<>(Arrays.asList(Cathegorie.DEUX_ROUES,Cathegorie.QUATRES_ROUES));
        Map<Cathegorie,Double> tarifs =new HashMap<Cathegorie, Double>() {{
            put(Cathegorie.QUATRES_ROUES,2.);
            put(Cathegorie.DEUX_ROUES, 1.);
        }};

        Parking parking = Parking.builder()
                .id(1l)
                .vehiculesAcceptee(cathegoriesAcceptés)
                .tarif(tarifs)
                .nombreDePlaceDisponoible(50)
                .mouvementVehicules(new ArrayList<>())
                .build();

        Vehicule voiture = Vehicule.builder()
                .id(1l)
                .typeCarburant(Carburant.ESSENCE)
                .cathegorie(Cathegorie.QUATRES_ROUES)
                .build();
        Vehicule moto = Vehicule.builder()
                .id(2l)
                .typeCarburant(Carburant.ESSENCE)
                .cathegorie(Cathegorie.DEUX_ROUES)
                .build();
        parkingGestionService.vehiculeEntrant(
                parking,
                voiture,
                LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(13,24))
        );
        parkingGestionService.vehiculeEntrant(
                parking,
                moto,
                LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(19,30))
        );

        parkingGestionService.vehiculeSortant(
                parking,
                voiture,
                LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(15,10))
        );
        parkingGestionService.vehiculeSortant(
                parking,
                moto,
                LocalDateTime.of(LocalDate.of(2020,6,23), LocalTime.of(0,37))
        );

        log.info(parkingGestionService.tempsPasseAuParking(parking, voiture));
        log.info(parkingGestionService.tempsPasseAuParking(parking, moto));
        log.info(parkingGestionService.montantDu(parking, voiture));
        log.info(parkingGestionService.montantDu(parking, moto));

        log.info(parking.toString());

    }

}

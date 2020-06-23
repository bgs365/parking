package com.bgs.parking.service;

import com.bgs.parking.model.Carburant;
import com.bgs.parking.model.Categories;
import com.bgs.parking.model.Parking;
import com.bgs.parking.model.Vehicule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class ParkingGestionServiceTest {

    private Parking parking;

    @Autowired
    private ParkingGestionService parkingGestionService;

    @BeforeEach
    void setUp() {
        Map<Categories,Double> tarifs =new HashMap<Categories, Double>() {{
            put(Categories.QUATRES_ROUES,2.);
            put(Categories.DEUX_ROUES, 1.);
        }};

        parking = Parking.builder()
                .id(1l)
                .tarif(tarifs)
                .nombreDePlaceDisponoible(50)
                .mouvementVehicules(new ArrayList<>())
                .build();
    }

    @ParameterizedTest
    @MethodSource("buildDataSourceForEntreeVehicule")
    void shouldAddOnevehicule(Vehicule vehicule, LocalDateTime heureEntree) {
        boolean bienAjoute = parkingGestionService.estVehiculeEntrant(parking, vehicule, heureEntree );

        assertThat(true).isEqualTo(bienAjoute);
        assertThat(vehicule).isEqualTo(parking.getMouvementVehicules().get(0).getVehicule());
        assertThat(heureEntree).isEqualTo(parking.getMouvementVehicules().get(0).getHeureEntree());
    }

    static Stream<Arguments> buildDataSourceForEntreeVehicule() {
        return Stream.of(
                arguments(
                        Vehicule.builder()
                        .id(1l)
                        .typeCarburant(Carburant.ESSENCE)
                        .categorie(Categories.QUATRES_ROUES)
                        .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(13,24))
                ),
                arguments(
                        Vehicule.builder()
                                .id(2l)
                                .typeCarburant(Carburant.ESSENCE)
                                .categorie(Categories.DEUX_ROUES)
                                .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(19,30))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("buildDataSourceForSortieVehicule")
    void shouldAddHeureSortie(Vehicule vehicule, LocalDateTime heureEntree, LocalDateTime heureSortie ) {
        parkingGestionService.estVehiculeEntrant(parking, vehicule, heureEntree );
        boolean estSortie = parkingGestionService.estVehiculeSortant(parking, vehicule, heureSortie );
        assertThat(true).isEqualTo(estSortie);
        assertThat(heureSortie).isEqualTo(parking.getMouvementVehicules().get(0).getHeureSortie());
    }

    static Stream<Arguments> buildDataSourceForSortieVehicule() {
        return Stream.of(
                arguments(
                        Vehicule.builder()
                        .id(1l)
                        .typeCarburant(Carburant.ESSENCE)
                        .categorie(Categories.QUATRES_ROUES)
                        .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(13,24)),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(15,10))
                ),
                arguments(
                        Vehicule.builder()
                                .id(2l)
                                .typeCarburant(Carburant.ESSENCE)
                                .categorie(Categories.DEUX_ROUES)
                                .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(19,30)),
                        LocalDateTime.of(LocalDate.of(2020,6,23), LocalTime.of(0,37))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("buildDataSourceForSortieVehiculeSansEntree")
    void shouldNotAddHeureSortie(Vehicule vehicule, LocalDateTime heureSortie ) {
        boolean estSortie = parkingGestionService.estVehiculeSortant(parking, vehicule, heureSortie );
        assertThat(false).isEqualTo(estSortie);
    }

    static Stream<Arguments> buildDataSourceForSortieVehiculeSansEntree() {
        return Stream.of(
                arguments(
                        Vehicule.builder()
                        .id(1l)
                        .typeCarburant(Carburant.ESSENCE)
                        .categorie(Categories.QUATRES_ROUES)
                        .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(15,10))
                ),
                arguments(
                        Vehicule.builder()
                                .id(2l)
                                .typeCarburant(Carburant.ESSENCE)
                                .categorie(Categories.DEUX_ROUES)
                                .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,23), LocalTime.of(0,37))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("buildDataSourceForDuration")
    void shouldHaveRigthDuration(Vehicule vehicule, LocalDateTime heureEntree, LocalDateTime heureSortie ) {
        parkingGestionService.estVehiculeEntrant(parking, vehicule, heureEntree );
        parkingGestionService.estVehiculeSortant(parking, vehicule, heureSortie );
        assertThat(Duration.between(heureEntree,heureSortie)).isEqualTo(parkingGestionService.tempsPasseAuParkingLorsDuDernierPasssage(parking, vehicule));
    }

    static Stream<Arguments> buildDataSourceForDuration() {
        return Stream.of(
                arguments(
                        Vehicule.builder()
                                .id(1l)
                                .typeCarburant(Carburant.ESSENCE)
                                .categorie(Categories.QUATRES_ROUES)
                                .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(13,24)),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(15,10))
                ),
                arguments(
                        Vehicule.builder()
                                .id(2l)
                                .typeCarburant(Carburant.ESSENCE)
                                .categorie(Categories.DEUX_ROUES)
                                .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(19,30)),
                        LocalDateTime.of(LocalDate.of(2020,6,23), LocalTime.of(0,37))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("buildDataSourceForPrice")
    void shouldHaveRightPrice(Vehicule vehicule, LocalDateTime heureEntree, LocalDateTime heureSortie, double prix ) {
        parkingGestionService.estVehiculeEntrant(parking, vehicule, heureEntree );
        parkingGestionService.estVehiculeSortant(parking, vehicule, heureSortie );
        assertThat(prix).isEqualTo(parkingGestionService.montantDu(parking, vehicule));
    }

    static Stream<Arguments> buildDataSourceForPrice() {
        return Stream.of(
                arguments(
                        Vehicule.builder()
                                .id(1l)
                                .typeCarburant(Carburant.ESSENCE)
                                .categorie(Categories.QUATRES_ROUES)
                                .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(13,24)),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(15,10)),
                        4
                ),
                arguments(
                        Vehicule.builder()
                                .id(2l)
                                .typeCarburant(Carburant.ESSENCE)
                                .categorie(Categories.DEUX_ROUES)
                                .build(),
                        LocalDateTime.of(LocalDate.of(2020,6,22), LocalTime.of(19,30)),
                        LocalDateTime.of(LocalDate.of(2020,6,23), LocalTime.of(0,37)),
                        6
                )
        );
    }

}

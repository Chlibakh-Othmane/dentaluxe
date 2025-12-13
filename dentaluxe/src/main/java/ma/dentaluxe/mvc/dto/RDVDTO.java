package ma.dentaluxe.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.StatutRDV;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RDVDTO {

    private Long idRDV;
    private Long idDM;
    private Long idMedecin;
    private LocalDate dateRDV;
    private LocalTime heureRDV;
    private StatutRDV statut;
    private String motif;
    private String notes;

    // Champs supplémentaires pour l'affichage (optionnels)
    private String nomPatient;
    private String prenomPatient;
    private String nomMedecin;
    private String prenomMedecin;
    private String specialiteMedecin;
    private String statutLibelle;
    private Boolean isPasse;
    private Boolean isAujourdhui;
    private Boolean isAVenir;
    private Integer dureeEstimee; // en minutes
    private LocalTime heureFinEstimee;
}

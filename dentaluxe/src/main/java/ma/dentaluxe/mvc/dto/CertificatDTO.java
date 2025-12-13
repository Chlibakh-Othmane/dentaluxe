package ma.dentaluxe.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificatDTO {

    private Long idCertif;
    private Long idDM;
    private Long idMedecin;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer duree;
    private String noteMedecin;

    // Champs supplémentaires pour l'affichage (optionnels)
    private String nomPatient;
    private String prenomPatient;
    private String nomMedecin;
    private String prenomMedecin;
    private Boolean estActif;
    private Boolean estExpire;
}
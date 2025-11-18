package ma.dentaluxe.entities.utilisateur;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Staff {
    private Long id;
    private BigDecimal salaire;
    private BigDecimal prime;
    private LocalDate dateRecrutement;
    private Integer soldeConge;

    // Informations héritées de Utilisateur
    private Utilisateur utilisateur;
}
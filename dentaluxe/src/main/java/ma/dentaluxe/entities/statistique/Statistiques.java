package ma.dentaluxe.entities.statistique;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.CategorieStatistique;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statistiques {
    private Long idStat;
    private Long idCabinet;
    private String nom;
    private CategorieStatistique categorie;
    private Double chiffre;
    private LocalDate dateCalcul;
}
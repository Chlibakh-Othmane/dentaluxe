package ma.dentaluxe.entities.caisse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Revenu {
    private Long idRevenu;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;
}
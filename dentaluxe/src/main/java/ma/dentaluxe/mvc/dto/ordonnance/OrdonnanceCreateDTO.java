package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrdonnanceCreateDTO {
    private Long idDM;
    private Long idMedecin;
    private LocalDate dateOrdonnance;
}
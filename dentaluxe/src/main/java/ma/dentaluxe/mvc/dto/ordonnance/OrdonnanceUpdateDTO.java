package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrdonnanceUpdateDTO {
    private LocalDate dateOrdonnance;
    private String remarques;
}
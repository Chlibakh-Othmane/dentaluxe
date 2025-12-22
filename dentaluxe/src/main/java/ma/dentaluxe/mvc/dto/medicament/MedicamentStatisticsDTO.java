package ma.dentaluxe.mvc.dto.medicament;

import lombok.*;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedicamentStatisticsDTO {
    private long totalMedicaments;
    private long remboursablesCount;
    private Double prixMoyen;
    private Map<String, Long> repartitionParType;
}
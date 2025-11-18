package ma.dentaluxe.entities.utilisateur;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Secretaire {
    private Long id;
    private String numCnss;
    private BigDecimal commission;

    // Informations héritées de Staff
    private Staff staff;
}
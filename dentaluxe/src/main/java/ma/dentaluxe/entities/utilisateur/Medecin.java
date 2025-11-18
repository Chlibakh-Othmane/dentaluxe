package ma.dentaluxe.entities.utilisateur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medecin {
    private Long id;
    private String specialite;
    private String agendaMedecin;

    // Informations héritées de Staff
    private Staff staff;
}
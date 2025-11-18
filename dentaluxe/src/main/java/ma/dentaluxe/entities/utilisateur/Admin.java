package ma.dentaluxe.entities.utilisateur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin {
    private Long id;

    // Informations héritées de Utilisateur
    private Utilisateur utilisateur;
}
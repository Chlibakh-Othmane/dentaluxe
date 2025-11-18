package ma.dentaluxe.entities.cabinet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CabinetMedical {
    private Long idUser;
    private String nom;
    private String email;
    private String logo;
    private String adresse;
    private String cin;
    private String tel;
    private String siteWeb;
    private String instagram;
    private String facebook;
    private String description;
}
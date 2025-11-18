package ma.dentaluxe.entities.notification;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.TypeNotification;
import ma.dentaluxe.entities.enums.PrioriteNotification;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private Long id;
    private Long idUser;
    private String titre;
    private String message;
    private LocalDateTime dateCreation;
    private TypeNotification type;
    private PrioriteNotification priorite;
}
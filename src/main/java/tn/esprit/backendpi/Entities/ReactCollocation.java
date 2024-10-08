package tn.esprit.backendpi.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReactCollocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idReact;
    Long idUser;
    Long idAnn;
    Boolean likes;
    Boolean dislikes;
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    User userReact;
    @JsonIgnore
    @ManyToOne

    private AnnouncementCollocation announcementCollocation;



}

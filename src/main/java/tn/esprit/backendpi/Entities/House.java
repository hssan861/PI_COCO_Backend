package tn.esprit.backendpi.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.backendpi.Entities.Enum.HouseType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idHouse;

    String imageUrl;
    @Enumerated(EnumType.STRING)
    HouseType houseType;
    Long places;
    String Location;
    String description;
    Long nbrofBedrooms; @ToString.Exclude
    String username;
    Long userId;
    String title;
    Long price;
    int budgetPart;

      Boolean contracted=false;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "houseAnnoCollocation")
    List<AnnouncementCollocation> announcementCollocationsHouse = new ArrayList<>();
    @ToString.Exclude
    @JsonIgnore
    @OneToOne
    Contract contractHouse;
}
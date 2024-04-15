package tn.esprit.backendpi.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idProduct;
   private String img;
    String description;
    float  price;
    String title;
    // String path;
    @Enumerated(EnumType.STRING)
    TypeCategory category;
    Boolean status;

    @ManyToOne
    Command commandProduct;
    @ManyToMany
    List<User>usersProducts;
}

package tn.esprit.backendpi.Entities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Getter
    @NotBlank
    @Size(max = 20)
    String username;
    @Getter
    @NotBlank
    @Size(max = 120)
    String password;
    @Getter
    @NotBlank
    @Size(max = 50)
    @Email
    String email;

    @Getter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    String fullname;
    String espritId;
    String niv;
    Long phone;
    String imageUrl="//ssl.gstatic.com/accounts/ui/avatar_2x.png";
    LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    GenderType gender;
    Long score;
    @ToString.Exclude
    @ManyToMany
    List<Claims> claimsUser;
    @ManyToMany
    List<RequirementCollocation> requirementCollocationsUser;
    @OneToOne
    Car carUser;
    @OneToOne
    Calendar calendarUser;
    @OneToOne
    Adress adressUser;
    @OneToMany(mappedBy = "userReact")
    List<ReactCollocation>reactsUser=new ArrayList<>();
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "userCommand")
    List<Command> commandsUser = new ArrayList<>();
    @ToString.Exclude
    @OneToMany(mappedBy = "userReactPost")
    List<ReactPost>reactPostuser=new ArrayList<>();
    @Getter
    @Setter
    @Column(name = "verification_code")
    private String verificationCode;
    @Setter
    @Getter
    @Column(name = "is_verified")
    private boolean verified =false;



}
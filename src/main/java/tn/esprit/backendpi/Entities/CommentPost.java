package tn.esprit.backendpi.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPost implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idCommentPost;
    String commentBody;
    LocalDate commentedAt;
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    User userCommentPost;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany
    List<ReactPost> reactPostsComment=new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postCo")
    Set<CommentPost> postComments; //Reflexive association : A comment can have multiple replies

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    CommentPost postCo;

    //@JsonIgnore
    @ToString.Exclude
    @ManyToOne
    Post postComment;
}

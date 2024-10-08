package tn.esprit.backendpi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.backendpi.Entities.Post;
import tn.esprit.backendpi.Entities.ReactPost;
import tn.esprit.backendpi.Entities.TypeReactPost;
import tn.esprit.backendpi.Entities.User;

import java.util.List;

@Repository
public interface ReactPostRepository extends JpaRepository<ReactPost,Long> {
    List<ReactPost> findByPostIdPost(Long postId);
    ReactPost findByPostAndUserReactPostAndTypeReact(Post post, User user, TypeReactPost typeReact);
    Long countByPostAndUserReactPost(Post post, User user);

    @Query("SELECT r FROM ReactPost r WHERE r.post.idPost = ?1 AND r.userReactPost.id = ?2")
    ReactPost findByPostIdAndUserReactPostId(Long postId, Long idUSer);
}

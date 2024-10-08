package tn.esprit.backendpi.Service.Interfaces;

import tn.esprit.backendpi.Entities.*;

import java.util.List;
import java.util.Optional;

public interface IPost {

    /*********     Post     **********/
    List<Post> retrieveAllPost();

    Post updatePost(Post p);

    Post addPost(Post p);

    Post retrievePost(long idPost);

    void removePost(long idPost);


    /*********     Comment Post     **********/
    List<CommentPost> retrieveAllCommentPost();

    CommentPost updateCommentPost(CommentPost c);


    CommentPost retrieveCommentPost(long idCommentPost);

    void removeCommentPost(long idCommentPost);


    /*********     React Post     **********/
    List<ReactPost> retrieveAllReactPost();

    ReactPost updateReactPost(ReactPost r);

    ReactPost addReactPost(ReactPost r);

    ReactPost retrieveReactPost(long idReactPost);

    void removeReactPost(long idReactPost);
    ReactPost checkExistingReaction(Long postId, TypeReactPost reactionType);


    /******* AVANCEE ******/

    List<CommentPost> getCommentsForPost(Long postId);
    CommentPost addCommenttoPost(CommentPost comment,Long IdPost);
    List<CommentPost> getReplies(Long commentId);
    CommentPost addCommentToComment(CommentPost comment, Long idComm);

    ReactPost addReacttoPost(ReactPost react , Long IdPost);
    List<ReactPost> getReactsForPost(Long postId);
    ReactPost addTypeReacttoPost(TypeReactPost typereact , Long IdPost);
    List<ReactPost> getReactsForComment(Long idComment);
    ReactPost addReactToComment(TypeReactPost typereact ,Long idcomment ) ;
    public Post MeilleurPost() ;
    String AddWithoutBadWord(Post post);
    //apres authentification

    String UserAddPost(Post post, Long idUser) ;
    CommentPost UseraddComment(CommentPost comment ,Long IdPost, Long idUser) ;
    CommentPost UseraddCommentToComment(CommentPost comment, Long idComm, Long idUser);
    ReactPost UseraddReacttoPost(ReactPost react , Long IdPost, Long idUser);
    ReactPost UseraddReactToComment(ReactPost react ,Long idcomment , Long idUser) ;
    void deletePostByTime();
    void reportPost(Long IdPost);
    void UpdatereportPost(Long postId) ;

    String UserAddWithoutBadWord(Post post, Long idUser);

    boolean countByUserReactPost(Long idPost);

    Optional<String> findUserNameAndLastNameByPostId(Long postId);
    Optional<String> findUserCommentPostByIdCommentPost(Long idCommentPost);

    void updatePostRating(Long postId, int nb_etoil);
    RaitingPost addRaitingPost(long postId, long nbStart);
    boolean hasUserRatedPost(long postId) ;

    int getNBuserRaited (Long postId);

    double AvrageRaitePost(Long postId);

    void updatePostRate(Long postId);

    void updateReact(Long idPost,ReactPost r);


    }
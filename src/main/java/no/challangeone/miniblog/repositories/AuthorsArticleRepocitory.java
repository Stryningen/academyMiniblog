package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.Comment;
import no.challangeone.miniblog.data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorsArticleRepocitory extends CrudRepository<AuthorArticle, Integer> {
    List<AuthorArticle> findByArticle(Article article);

    List<AuthorArticle> findByUser(User user);

    @Query("" +
            "SELECT c FROM Comment c " +
            "LEFT JOIN Article a ON a.id = c.article.id " +
            "LEFT JOIN User u ON u.id = c.user.id " +
            "WHERE c.article.id = ?1 " +
            "AND c.user.id =?2")
    List<Comment> findByArticleAndAuthor(Integer articleId, Integer userId);

    AuthorArticle findByUserAndArticle(User user, Article article);
}

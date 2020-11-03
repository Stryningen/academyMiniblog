package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorsArticleRepocitory extends CrudRepository<AuthorArticle, Integer> {
    List<AuthorArticle> findByArticle(Article article);

    List<AuthorArticle> findByUser(User user);
}

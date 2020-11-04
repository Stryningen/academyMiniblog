package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findByArticle(Article article);
}

package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
}

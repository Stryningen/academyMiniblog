package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserName(String userName);

    User findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN AuthorArticle aa on u.id = aa.user.id " +
            "WHERE aa.article.id = ?1 " +
            "GROUP BY u.id")
    List<User> findByArticle(Integer articleId);
}

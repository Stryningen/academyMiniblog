package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
}

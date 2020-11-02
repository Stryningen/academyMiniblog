package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Authors;
import org.springframework.data.repository.CrudRepository;

public interface AuthorsRepocitory extends CrudRepository<Authors, Integer> {
}

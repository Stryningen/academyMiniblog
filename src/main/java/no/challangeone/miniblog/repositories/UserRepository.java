package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserName(String userName);
}

package no.challangeone.miniblog.security;

import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurtiyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public SecurtiyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);

        if (user == null){
            throw new UsernameNotFoundException(username);
        }
        return new SecurityUserDetails(user);


    }
}

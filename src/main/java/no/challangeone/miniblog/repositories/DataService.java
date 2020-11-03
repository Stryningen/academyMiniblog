package no.challangeone.miniblog.repositories;

import org.springframework.stereotype.Service;

@Service
public class DataService {

    private ArticleRepository articleRepository;
    private AuthorsRepocitory authorsRepocitory;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    public DataService(
            ArticleRepository articleRepository, AuthorsRepocitory authorsRepocitory,
            CommentRepository commentRepository, UserRepository userRepository
    ) {
        this.articleRepository = articleRepository;
        this.authorsRepocitory = authorsRepocitory;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }
}

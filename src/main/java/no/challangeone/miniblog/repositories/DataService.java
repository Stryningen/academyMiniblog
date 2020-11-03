package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.User;
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

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void deleteUserByUserName(String userName) {
        User user = findUserByName(userName);
        userRepository.delete(user);
    }

    public Article findArticleByArticleName(String articleName) {
        return articleRepository.findByArticleName(articleName);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticleByArticleName(String articleName) {
        Article article = findArticleByArticleName(articleName);
        articleRepository.delete(article);
    }
}

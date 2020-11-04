package no.challangeone.miniblog.repositories;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.Comment;
import no.challangeone.miniblog.data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private ArticleRepository articleRepository;
    private AuthorsArticleRepocitory authorsArticleRepocitory;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    public DataService(
            ArticleRepository articleRepository, AuthorsArticleRepocitory authorsArticleRepocitory,
            CommentRepository commentRepository, UserRepository userRepository
    ) {
        this.articleRepository = articleRepository;
        this.authorsArticleRepocitory = authorsArticleRepocitory;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public Article findArticleByArticleName(String articleName) {
        return articleRepository.findByArticleName(articleName);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    public AuthorArticle saveAuthorArticle(AuthorArticle authorArticle) {
        return authorsArticleRepocitory.save(authorArticle);
    }

    public List<AuthorArticle> findAuthorArticleByArticle(Article article) {
        return authorsArticleRepocitory.findByArticle(article);
    }

    public List<AuthorArticle> findAuthorArticleByAuthor(User user) {
        return authorsArticleRepocitory.findByUser(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Comment> findCommentsByArticle(Article article) {
        return commentRepository.findByArticle(article);
    }

    public List<Comment> saveAllComments(List<Comment> comments){
        return (List<Comment>) commentRepository.saveAll(comments);
    }


    public List<Comment> findCommentsByArticleAndAuthor(Article article, User user){
        return authorsArticleRepocitory.findByArticleAndAuthor(article.getId(), user.getId());
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}

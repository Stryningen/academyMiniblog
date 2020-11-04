package no.challangeone.miniblog;

import com.github.javafaker.Faker;
import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.Comment;
import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class MockDataController {
    //For setting up mock data in the db

    DataService dataService;
    DataHandler dataHandler;
    Faker faker;

    public MockDataController(DataService dataService, DataHandler dataHandler) {
        this.dataService = dataService;
        this.dataHandler = dataHandler;
        this.faker = new Faker();
    }

    @GetMapping("/mock/init")
    public String initMockData() {
        String reply = "";
        if (dataService.findAllUsers().size() < 25) {
            setUpUsers();
            reply += "users: added,";
        } else {
            reply += "users: already populated,";
        }

        if (dataService.findAllArticles().size() < 50) {
            setUpArticles();
            reply += "\narticles: added,";
        } else {
            reply += "\narticles: already populated,";
        }

        if (dataService.findAllAuthorArticles().size() < 50) {
            setUpAuthors();
            reply += "\nauthors: added,";
        } else {
            reply += "\nauthors: already populated,";
        }

        if(dataService.findAllComments().size() < 500){
            setUpComments();
            reply += "\ncomments: added,";
        } else {
            reply += "\ncomments: already populated,";
        }


        return reply;
    }

    private void setUpComments() {
        List<Article> articles = dataService.findAllArticles();
        List<User> users = dataService.findAllUsers();
        Timestamp now = dataHandler.timestampNow();
        User randomUser = null;
        Comment comment;
        String message;

        for (Article article : articles) {
            if (dataService.findCommentsByArticle(article).size() <= 20) {
                for (int i = 0; i < faker.random().nextInt(0, 20); i++) {
                    randomUser = users.get(faker.random().nextInt(0, users.size() - 1));
                    message = faker.backToTheFuture().quote();
                    comment = new Comment(
                            randomUser, article,
                            dataHandler.addDays(now, -faker.random().nextInt(0, 30)),
                            message
                    );
                    dataService.saveComment(comment);
                }
            }
        }
    }

    private void setUpAuthors() {
        List<Article> articles = dataService.findAllArticles();
        List<User> users = dataService.findAllUsers();

        for (Article article : articles) {
            User randomUser;
            AuthorArticle authArt;
            for (int i = 0; i < faker.random().nextInt(1, 3); i++) {
                randomUser = users.get(faker.random().nextInt(0, users.size() - 1));
                authArt = dataService.findAuthorArticleByAuthorAndArticle(randomUser, article);
                if (authArt != null) {
                    dataService.deleteAuthor(authArt);
                }
                authArt = new AuthorArticle(randomUser, article);
                dataService.saveAuthorArticle(authArt);
            }
        }
    }

    private void setUpArticles() {
        String articleName;
        Article article;
        Timestamp now = dataHandler.timestampNow();
        for (int i = 0; i < 50; i++) {
            articleName = faker.animal().name();
            article = dataService.findArticleByArticleName(articleName);
            if (article != null) {
                dataService.deleteArticle(article);
            }
            article = new Article(
                    articleName,
                    dataHandler.addDays(now, -(faker.random().nextInt(0, 30)))
            );
            article.setArticleBody("<p>" +
                    String.join("</p><p>",
                            faker.lorem().paragraphs(faker.random().nextInt(1, 7))
                    )
                    + "</p>"
            );
            dataService.saveArticle(article);
        }
    }

    private void setUpUsers() {

        String userName;
        User user;
        for (int i = 0; i < 25; i++) {
            userName = faker.starTrek().character();
            user = dataService.findUserByName(userName);
            if (user != null) {
                dataService.deleteUser(user);
            }
            user = new User(
                    userName,
                    faker.pokemon().name() + "@" + faker.space().star(),
                    "1234"
            );
            dataService.saveUser(user);
        }
    }
}

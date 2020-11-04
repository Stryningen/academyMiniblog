package no.challangeone.miniblog;

import com.github.javafaker.Faker;
import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
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
        if(dataService.findAllUsers().size() < 25){
            List<User> users = setUpUsers();
            reply += "users: added,";
        } else {
            reply += "users: already populated,";
        }

        if (dataService.findAllArticles().size() < 50){
            List<Article> articles = setUpArticles();
            reply += "\narticles: added,";
        } else {
            reply += "\narticles: already populated,";
        }

        return reply;
    }

    private List<Article> setUpArticles() {
        List<Article> articles = new ArrayList<>();
        String articleName;
        Article article;
        Timestamp now = dataHandler.timestampNow();
        for (int i = 0; i < 50; i++){
            articleName = faker.animal().name();
            article = dataService.findArticleByArticleName(articleName);
            if (article != null){
                dataService.deleteArticle(article);
            }
            article = new Article(
                    articleName,
                    dataHandler.addDays(now, -(faker.random().nextInt(0, 30)))
            );
            articles.add(article);
            dataService.saveArticle(article);
        }
        return articles;
    }

    private List<User> setUpUsers() {

        List<User> users = new ArrayList<>();
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
            users.add(user);
            dataService.saveUser(user);
        }

        return users;
    }
}

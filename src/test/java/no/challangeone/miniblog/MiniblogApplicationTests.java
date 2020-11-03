package no.challangeone.miniblog;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MiniblogApplicationTests {

    @Autowired
    private DataService dataService;
    @Autowired
    private DataHandler dataHandler;

    @Test
    void contextLoads() {
    }

    @Test
    public void testUserRepository() {

        if (dataService.findUserByName("test_user") != null) {
            dataService.deleteUserByUserName("test_user");
        }
        User user = new User("test_user", "test@test.no", "1234");
        dataService.saveUser(user);

        User userToCheck = dataService.findUserByName("test_user");
        assertAll("user",
                () -> assertEquals("test_user", userToCheck.getUserName()),
                () -> assertEquals("test@test.no", userToCheck.getEmail()),
                () -> assertEquals("1234", userToCheck.getPassword())
        );

        dataService.deleteUserByUserName("test_user");
        assertNull(dataService.findUserByName("test_user"));

    }

    @Test
    public void testArticleRepository() {
        if (dataService.findArticleByArticleName("test_article") != null) {
            dataService.deleteArticleByArticleName("test_article");
        }
        Timestamp now = dataHandler.timestampNow();
        Article article = new Article("test_article", now);
        dataService.saveArticle(article);

        Article articleToCheck = dataService.findArticleByArticleName("test_article");
        System.out.println(now);
        System.out.println(articleToCheck.getCreatedDate());
        assertAll("article",
                () -> assertEquals("test_article", articleToCheck.getArticleName()),
                () -> assertEquals(now, articleToCheck.getCreatedDate())
        );

        dataService.deleteArticleByArticleName("test_article");
        assertNull(dataService.findArticleByArticleName("test_article"));
    }

}

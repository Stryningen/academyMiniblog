package no.challangeone.miniblog;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        User user = dataService.findUserByName("test_user");
        if (user != null) {
            dataService.deleteUser(user);
        }
        user = new User("test_user", "test@test.no", "1234");
        dataService.saveUser(user);

        User userToCheck = dataService.findUserByName("test_user");
        assertAll("user",
                () -> assertEquals("test_user", userToCheck.getUserName()),
                () -> assertEquals("test@test.no", userToCheck.getEmail()),
                () -> assertEquals("1234", userToCheck.getPassword())
        );

        dataService.deleteUser(userToCheck);
        assertNull(dataService.findUserByName("test_user"));

    }

    @Test
    public void testArticleRepository() {
        Article article = dataService.findArticleByArticleName("test_article");
        if (article != null) {
            dataService.deleteArticle(article);
        }
        Timestamp now = dataHandler.timestampNow();
        article = new Article("test_article", now);
        dataService.saveArticle(article);

        Article articleToCheck = dataService.findArticleByArticleName("test_article");
        System.out.println(now);
        System.out.println(articleToCheck.getCreatedDate());
        assertAll("article",
                () -> assertEquals("test_article", articleToCheck.getArticleName()),
                () -> assertEquals(now, articleToCheck.getCreatedDate())
        );

        dataService.deleteArticle(articleToCheck);
        assertNull(dataService.findArticleByArticleName("test_article"));
    }

    @Test
    public void testAuthorsRepository() {
        List<User> listOfUsers = Arrays.asList(
                new User("test1", "test1@test.no", "1234"),
                new User("test2", "test2@test.no", "1234"),
                new User("test3", "test3@test.no", "1234")
        );

        for (User user : listOfUsers) {
            dataService.saveUser(user);
        }

        Timestamp now = dataHandler.timestampNow();

        List<Article> listOfArticles = Arrays.asList(
                new Article("test_article_1", now),
                new Article("test_article_2", dataHandler.addDays(now, -1)),
                new Article("test_article_3", dataHandler.addDays(now, -1)),
                new Article("test_article_4", dataHandler.addDays(now, -2)),
                new Article("test_article_5", dataHandler.addDays(now, -5))
        );

        for (int i = 0; i < listOfArticles.size(); i++) {
            dataService.saveArticle(listOfArticles.get(i));
            dataService.saveAuthor(new AuthorArticle(listOfUsers.get(0), listOfArticles.get(i)));
        }
        dataService.saveAuthor(new AuthorArticle(listOfUsers.get(1), listOfArticles.get(1)));
        dataService.saveAuthor(new AuthorArticle(listOfUsers.get(1), listOfArticles.get(2)));
        dataService.saveAuthor(new AuthorArticle(listOfUsers.get(2), listOfArticles.get(1)));
        dataService.saveAuthor(new AuthorArticle(listOfUsers.get(2), listOfArticles.get(3)));

        List<AuthorArticle> listOfAllAuthorArticles = new ArrayList<>();
        for (Article article : listOfArticles) {
            listOfAllAuthorArticles.addAll(dataService.findAuthorArticleByArticle(article));
        }
        assertEquals(9, listOfAllAuthorArticles.size());

        assertEquals(1, dataService.findAuthorArticleByArticle(listOfArticles.get(0)).size());
        assertEquals(3, dataService.findAuthorArticleByArticle(listOfArticles.get(1)).size());

        assertEquals(5, dataService.findAuthorArticleByAuthor(listOfUsers.get(0)).size());
        assertEquals(2, dataService.findAuthorArticleByAuthor(listOfUsers.get(2)).size());

        Article deletedArticle = listOfArticles.get(0);
        dataService.deleteArticle(deletedArticle);
        assertEquals(0, dataService.findAuthorArticleByArticle(deletedArticle).size());

        for (User user: listOfUsers){
            dataService.deleteUser(user);
            assertEquals(0, dataService.findAuthorArticleByAuthor(user).size());
        }

        for (Article article: listOfArticles){
            dataService.deleteArticle(article);
        }
    }


}

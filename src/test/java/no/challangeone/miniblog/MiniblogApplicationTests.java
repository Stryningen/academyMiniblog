package no.challangeone.miniblog;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.Comment;
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

        User userToCheckName = dataService.findUserByName("test_user");
        assertAll("user name:",
                () -> assertEquals("test_user", userToCheckName.getUserName()),
                () -> assertEquals("test@test.no", userToCheckName.getEmail()),
                () -> assertEquals("1234", userToCheckName.getPassword())
        );

        User userToCheckEmail = dataService.findUserByEmail("test@test.no");
        assertAll("user email:",
                () -> assertEquals("test_user", userToCheckEmail.getUserName()),
                () -> assertEquals("test@test.no", userToCheckEmail.getEmail()),
                () -> assertEquals("1234", userToCheckEmail.getPassword())
        );

        dataService.deleteUser(userToCheckName);
        assertNull(dataService.findUserByName("test_user"));
        assertNull(dataService.findUserByEmail("test@test.no"));
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
    public void testAuthorArticleRepository() {
        List<User> listOfUsers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            User user = dataService.findUserByName("test"+i);
            if (user != null){
                dataService.deleteUser(user);
            }
          listOfUsers.add(new User("test"+i, "test"+i+"@test.no", "1234"));
            dataService.saveUser(listOfUsers.get(i-1));
        }

        Timestamp now = dataHandler.timestampNow();
        List<Article> listOfArticles = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Article article = dataService.findArticleByArticleName("test_article_"+i);
            if (article != null){
                dataService.deleteArticle(article);
            }
            listOfArticles.add(new Article("test_article_"+i, dataHandler.addDays(now, -i)));
            dataService.saveArticle(listOfArticles.get(i-1));
            dataService.saveAuthorArticle(new AuthorArticle(listOfUsers.get(0), listOfArticles.get(i-1)));
        }

        dataService.saveAuthorArticle(new AuthorArticle(listOfUsers.get(1), listOfArticles.get(1)));
        dataService.saveAuthorArticle(new AuthorArticle(listOfUsers.get(1), listOfArticles.get(2)));
        dataService.saveAuthorArticle(new AuthorArticle(listOfUsers.get(2), listOfArticles.get(1)));
        dataService.saveAuthorArticle(new AuthorArticle(listOfUsers.get(2), listOfArticles.get(3)));

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

        for (User user : listOfUsers) {
            dataService.deleteUser(user);
            assertEquals(0, dataService.findAuthorArticleByAuthor(user).size());
        }

        for (Article article : listOfArticles) {
            dataService.deleteArticle(article);
        }
    }

    @Test
    public void testCommentRepository() {
        List<User> listOfUsers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            User user = dataService.findUserByName("test"+i);
            if (user != null){
                dataService.deleteUser(user);
            }
            listOfUsers.add(new User("test"+i, "test"+i+"@test.no", "1234"));
            dataService.saveUser(listOfUsers.get(i-1));
        }

        Timestamp now = dataHandler.timestampNow();
        List<Article> listOfArticles = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Article article = dataService.findArticleByArticleName("test_article_"+i);
            if (article != null){
                dataService.deleteArticle(article);
            }
            listOfArticles.add(new Article("test_article_"+i, dataHandler.addDays(now, -i)));
            dataService.saveArticle(listOfArticles.get(i-1));
            dataService.saveAuthorArticle(new AuthorArticle(listOfUsers.get(0), listOfArticles.get(i-1)));
        }

        List<Comment> listOfComments = new ArrayList<>();
        String comment = "This is a comment! 4488";
        for (User user : listOfUsers) {
            listOfComments.addAll(
                    Arrays.asList(
                            new Comment(user, listOfArticles.get(0), now, comment),
                            new Comment(user, listOfArticles.get(1), now, comment)
                    )
            );
        }
        comment = "Changed it because I can! 3399";
        listOfComments.addAll(
                Arrays.asList(
                        new Comment(listOfUsers.get(0), listOfArticles.get(0), now, comment),
                        new Comment(listOfUsers.get(1), listOfArticles.get(0), now, comment),
                        new Comment(listOfUsers.get(1), listOfArticles.get(0), now, comment),
                        new Comment(listOfUsers.get(2), listOfArticles.get(1), now, comment)
                )
        );

        dataService.saveAllComments(listOfComments);

        assertEquals(6, dataService.findCommentsByArticle(listOfArticles.get(0)).size());
        assertEquals(4, dataService.findCommentsByArticle(listOfArticles.get(1)).size());
        assertEquals(2, dataService.findCommentsByArticleAndAuthor(listOfArticles.get(1),listOfUsers.get(2)).size());
        assertEquals(3, dataService.findCommentsByArticleAndAuthor(listOfArticles.get(0),listOfUsers.get(1)).size());

        for (User user: listOfUsers){
            dataService.deleteUser(user);
        }
        for (Article article: listOfArticles){
            dataService.deleteArticle(article);
        }
    }


}

package no.challangeone.miniblog;

import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiniblogApplicationTests {

    @Autowired
    private DataService dataService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testUserRepository(){

        User user = new User();
    }

}

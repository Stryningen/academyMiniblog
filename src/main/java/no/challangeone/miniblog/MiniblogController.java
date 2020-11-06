package no.challangeone.miniblog;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MiniblogController {

    DataService dataService;

    public MiniblogController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/")
    public String index(Model model){

        List<Article> articles = dataService.findAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/article/{articleId}/details")
    public String articleDetails(Model model, @PathVariable Integer articleId){

        Article article = dataService.findArticleById(articleId);
        List<User> authors = dataService.findAuthorsByArticle(article);
        model.addAttribute("article", article);
        model.addAttribute("authors", authors);

        return "articleDetails";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}

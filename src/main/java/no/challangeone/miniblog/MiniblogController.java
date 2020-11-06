package no.challangeone.miniblog;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class MiniblogController {

    DataService dataService;
    DataHandler dataHandler;

    public MiniblogController(DataService dataService, DataHandler dataHandler) {
        this.dataService = dataService;
        this.dataHandler = dataHandler;
    }

    @GetMapping("/")
    public String index(Model model, Principal principal){
        checkIfLoggedIn(model, principal);

        List<Article> articles = dataService.findAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }


    @GetMapping("/article/{articleId}/details")
    public String articleDetails(Model model, @PathVariable Integer articleId, Principal principal){
        checkIfLoggedIn(model, principal);

        Article article = dataService.findArticleById(articleId);
        checkIfAuthor(model, principal, article);
        List<User> authors = dataService.findAuthorsByArticle(article);
        model.addAttribute("article", article);
        model.addAttribute("authors", authors);

        return "articleDetails";
    }

    private boolean checkIfAuthor(Model model, Principal principal, Article article) {

        User user = dataService.findUserByName(principal.getName());
        if (dataService.findAuthorArticleByAuthorAndArticle(user, article) != null){
            model.addAttribute("author", true);
            return true;
        }
        model.addAttribute("author", false);
        return false;
    }

    @GetMapping(value = {"/createArticle", "/editArticle/{articleId}"})
    public String createOrEditArticle(Model model, Principal principal,
                                      @PathVariable(required = false) Integer articleId
    ){
        checkIfLoggedIn(model, principal);
        Article article = new Article();
        if (articleId != null){
            article = dataService.findArticleById(articleId);
        }
        model.addAttribute("article", article);
        return "articleForm";
    }

    @PostMapping("/saveArticle")
    public String saveArticle(Model model, @ModelAttribute @Valid Article article, BindingResult br, Principal principal){


        if (br.hasErrors()){
            return "articleForm";
        }

        if (article.getId() != null && dataService.checkIfArticleExists(article.getId())){
            article.setCreatedDate(dataService.findArticleById(article.getId()).getCreatedDate());
            dataService.saveArticle(article);
            return "redirect:/article/" + article.getId() + "/details";
        }



        article.setCreatedDate(dataHandler.timestampNow());
        User user = dataService.findUserByName(principal.getName());
        AuthorArticle author = new AuthorArticle(user, article);
        dataService.saveArticle(article);
        dataService.saveAuthorArticle(author);
        return "redirect:/article/" + article.getId() + "/details";
    }

    @GetMapping("/delete/article/{articleId}")
    public String deleteArticle(@PathVariable Integer articleId){
        dataService.deleteArticle(dataService.findArticleById(articleId));
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    private boolean checkIfLoggedIn(Model model, Principal principal) {
        model.addAttribute("loggedIn", true);
        if (principal == null){
            model.addAttribute("loggedIn", false);
            return false;
        }
        return true;
    }
}

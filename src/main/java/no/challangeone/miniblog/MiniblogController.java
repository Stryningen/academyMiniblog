package no.challangeone.miniblog;

import no.challangeone.miniblog.data.Article;
import no.challangeone.miniblog.data.AuthorArticle;
import no.challangeone.miniblog.data.Comment;
import no.challangeone.miniblog.data.User;
import no.challangeone.miniblog.repositories.DataService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    PasswordEncoder encoder;

    public MiniblogController(DataService dataService, DataHandler dataHandler,
                              PasswordEncoder encoder
    ) {
        this.dataService = dataService;
        this.dataHandler = dataHandler;
        this.encoder = encoder;
    }

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        checkIfLoggedIn(model, principal);

        List<Article> articles = dataService.findAllArticles();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/register")
    public String registerUser(Model model, Principal principal) {
        if (checkIfLoggedIn(model, principal)) {
            return "redirect:/";
        }
        User user = new User();
        model.addAttribute("user", user);
        return "registrationForm";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid User user, BindingResult br) {
        if (!br.hasErrors() && dataService.checkIfUserExist(user)) {
            ObjectError error = new ObjectError("user_exists", "user already exists");
            br.addError(error);
        }
        if (!br.hasErrors() && dataService.checkIfUserEmailExist(user)) {
            ObjectError error = new ObjectError("user_email_exists", "this email is already registered");
            br.addError(error);
        }
        if (br.hasErrors()) {
            return "registrationForm";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        dataService.saveUser(user);

        return "redirect:/login";
    }


    @GetMapping(value = {"/article/{articleId}/details",
            "/article/{articleId}/details/edit/comment/{commentId}"})
    public String articleDetails(Model model, @PathVariable Integer articleId,
                                 Principal principal, @PathVariable(required = false) Integer commentId
    ) {
        if (checkIfLoggedIn(model, principal)) {
            User user = dataService.findUserByName(principal.getName());
            model.addAttribute("userId", user.getId());
        }

        Comment newComment = new Comment();
        if (commentId != null){
            newComment = dataService.findCommentById(commentId);
        }
        model.addAttribute("newComment", newComment);

        Article article = dataService.findArticleById(articleId);
        checkIfAuthor(model, principal, article);

        List<User> authors = dataService.findAuthorsByArticle(article);
        model.addAttribute("article", article);
        model.addAttribute("authors", authors);

        List<Comment> comments = dataService.findCommentsByArticle(article);
        model.addAttribute("comments", comments);


        return "articleDetails";
    }

    private boolean checkIfAuthor(Model model, Principal principal, Article article) {

        User user = dataService.findUserByName(principal.getName());
        if (dataService.findAuthorArticleByAuthorAndArticle(user, article) != null) {
            model.addAttribute("author", true);
            return true;
        }
        model.addAttribute("author", false);
        return false;
    }

    @GetMapping(value = {"/createArticle", "/editArticle/{articleId}"})
    public String createOrEditArticle(Model model, Principal principal,
                                      @PathVariable(required = false) Integer articleId
    ) {
        checkIfLoggedIn(model, principal);
        Article article = new Article();
        if (articleId != null) {
            article = dataService.findArticleById(articleId);
        }
        model.addAttribute("article", article);
        return "articleForm";
    }

    @PostMapping("/saveArticle")
    public String saveArticle(Model model, @ModelAttribute @Valid Article article, BindingResult br, Principal principal) {


        if (br.hasErrors()) {
            return "articleForm";
        }

        if (article.getId() != null && dataService.checkIfArticleExists(article.getId())) {
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
    public String deleteArticle(@PathVariable Integer articleId) {
        dataService.deleteArticle(dataService.findArticleById(articleId));
        return "redirect:/";
    }

    @PostMapping("/postComment/{articleId}")
    public String postComment(@PathVariable Integer articleId, @ModelAttribute Comment newComment, BindingResult br,
                              Principal principal
                              ){
        if(br.hasErrors()){
            return "redirect:/article/"+articleId+"/details";
        }
        User user = dataService.findUserByName(principal.getName());
        Article article = dataService.findArticleById(articleId);
        newComment.setUser(user);
        newComment.setArticle(article);
        newComment.setCreatedDate(dataHandler.timestampNow());
        dataService.saveComment(newComment);
        return "redirect:/article/"+articleId+"/details";
    }

    @GetMapping("/delete/comment/{commentId}/{articleId}")
    public String deleteComment(@PathVariable Integer commentId, @PathVariable Integer articleId){
        Comment comment = dataService.findCommentById(commentId);
        dataService.deleteComment(comment);
        return "redirect:/article/"+articleId+"/details";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    private boolean checkIfLoggedIn(Model model, Principal principal) {
        model.addAttribute("loggedIn", true);
        if (principal == null) {
            model.addAttribute("loggedIn", false);
            return false;
        }
        return true;
    }
}

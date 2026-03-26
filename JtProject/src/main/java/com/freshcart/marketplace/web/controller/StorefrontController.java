package com.freshcart.marketplace.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.freshcart.marketplace.application.service.CustomerAccountService;
import com.freshcart.marketplace.application.service.ProductCatalogService;
import com.freshcart.marketplace.domain.entity.Customer;
import com.freshcart.marketplace.domain.entity.Merchandise;

@Controller
public class StorefrontController {

    private static final Logger LOGGER = Logger.getLogger(StorefrontController.class.getName());

    private final CustomerAccountService accountService;
    private final ProductCatalogService catalogService;

    public StorefrontController(CustomerAccountService accountService, ProductCatalogService catalogService) {
        this.accountService = accountService;
        this.catalogService = catalogService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/buy")
    public String showBuyPage() {
        return "buy";
    }

    @GetMapping("/login")
    public ModelAndView showLoginPage(@RequestParam(required = false) String error) {
        ModelAndView view = new ModelAndView("userLogin");
        if ("true".equals(error)) {
            view.addObject("msg", "Please enter correct email and password");
        }
        return view;
    }

    @GetMapping("/")
    public ModelAndView showHomepage() {
        ModelAndView view = new ModelAndView("index");
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        view.addObject("username", currentUser);

        List<Merchandise> inventory = this.catalogService.listAllProducts();
        if (inventory.isEmpty()) {
            view.addObject("msg", "No products are available");
        } else {
            view.addObject("products", inventory);
        }
        return view;
    }

    @GetMapping("/user/products")
    public ModelAndView browseCatalog() {
        ModelAndView view = new ModelAndView("uproduct");
        List<Merchandise> inventory = this.catalogService.listAllProducts();

        if (inventory.isEmpty()) {
            view.addObject("msg", "No products are available");
        } else {
            view.addObject("products", inventory);
        }
        return view;
    }

    @RequestMapping(value = "newuserregister", method = RequestMethod.POST)
    public ModelAndView processRegistration(@ModelAttribute Customer customer) {
        boolean taken = this.accountService.isUsernameTaken(customer.getUsername());

        if (!taken) {
            LOGGER.log(Level.INFO, "Registering new user: {0}", customer.getUsername());
            customer.setRole("ROLE_NORMAL");
            this.accountService.registerCustomer(customer);
            return new ModelAndView("userLogin");
        }

        LOGGER.log(Level.WARNING, "Username already taken: {0}", customer.getUsername());
        ModelAndView view = new ModelAndView("register");
        view.addObject("msg", customer.getUsername() + " is taken. Please choose a different username.");
        return view;
    }

    @GetMapping("/profileDisplay")
    public String showProfile(Model model, HttpServletRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = this.accountService.findByUsername(currentUser);

        if (customer != null) {
            model.addAttribute("userid", customer.getId());
            model.addAttribute("username", customer.getUsername());
            model.addAttribute("email", customer.getEmail());
            model.addAttribute("password", customer.getPassword());
            model.addAttribute("address", customer.getAddress());
        } else {
            model.addAttribute("msg", "User not found");
        }
        return "updateProfile";
    }

    @GetMapping("/test")
    public String debugView(Model model) {
        LOGGER.info("Debug view accessed");
        model.addAttribute("author", "jay gajera");
        model.addAttribute("id", 40);

        List<String> friends = new ArrayList<>();
        friends.add("xyz");
        friends.add("abc");
        model.addAttribute("f", friends);

        return "test";
    }

    @GetMapping("/test2")
    public ModelAndView debugViewAlt() {
        LOGGER.info("Alternate debug view accessed");
        ModelAndView view = new ModelAndView("test2");
        view.addObject("name", "jay gajera 17");
        view.addObject("id", 40);

        List<Integer> marks = new ArrayList<>();
        marks.add(10);
        marks.add(25);
        view.addObject("marks", marks);

        return view;
    }
}

package com.freshcart.marketplace.web.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.freshcart.marketplace.application.service.CategoryManagementService;
import com.freshcart.marketplace.application.service.CustomerAccountService;
import com.freshcart.marketplace.application.service.ProductCatalogService;
import com.freshcart.marketplace.domain.entity.Customer;
import com.freshcart.marketplace.domain.entity.Merchandise;
import com.freshcart.marketplace.domain.entity.ProductGroup;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private static final Logger LOGGER = Logger.getLogger(AdminPanelController.class.getName());

    private final CustomerAccountService accountService;
    private final CategoryManagementService categoryService;
    private final ProductCatalogService catalogService;

    public AdminPanelController(
            CustomerAccountService accountService,
            CategoryManagementService categoryService,
            ProductCatalogService catalogService) {
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.catalogService = catalogService;
    }

    @GetMapping("/index")
    public String showIndex(Model model) {
        String currentAdmin = resolveCurrentUsername();
        model.addAttribute("username", currentAdmin);
        return "index";
    }

    @GetMapping("login")
    public ModelAndView showAdminLogin(@RequestParam(required = false) String error) {
        ModelAndView view = new ModelAndView("adminlogin");
        if ("true".equals(error)) {
            view.addObject("msg", "Invalid username or password. Please try again.");
        }
        return view;
    }

    @GetMapping(value = {"/", "Dashboard"})
    public ModelAndView showDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView view = new ModelAndView("adminHome");
        view.addObject("admin", auth.getName());
        return view;
    }

    // --- Category management ---

    @GetMapping("categories")
    public ModelAndView listCategories() {
        ModelAndView view = new ModelAndView("categories");
        List<ProductGroup> groups = this.categoryService.listAllCategories();
        view.addObject("categories", groups);
        return view;
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam("categoryname") String categoryName) {
        LOGGER.log(Level.INFO, "Creating category: {0}", categoryName);
        this.categoryService.createCategory(categoryName);
        return "redirect:categories";
    }

    @GetMapping("categories/delete")
    public String deleteCategory(@RequestParam("id") int id) {
        this.categoryService.removeCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("categories/update")
    public String updateCategory(
            @RequestParam("categoryid") int id,
            @RequestParam("categoryname") String categoryName) {
        this.categoryService.renameCategory(id, categoryName);
        return "redirect:/admin/categories";
    }

    // --- Product management ---

    @GetMapping("products")
    public ModelAndView listProducts() {
        ModelAndView view = new ModelAndView("products");
        List<Merchandise> inventory = this.catalogService.listAllProducts();

        if (inventory.isEmpty()) {
            view.addObject("msg", "No products are available");
        } else {
            view.addObject("products", inventory);
        }
        return view;
    }

    @GetMapping("products/add")
    public ModelAndView showAddProductForm() {
        ModelAndView view = new ModelAndView("productsAdd");
        List<ProductGroup> groups = this.categoryService.listAllCategories();
        view.addObject("categories", groups);
        return view;
    }

    @RequestMapping(value = "products/add", method = RequestMethod.POST)
    public String handleAddProduct(
            @RequestParam("name") String name,
            @RequestParam("categoryid") int categoryId,
            @RequestParam("price") int price,
            @RequestParam("weight") int weight,
            @RequestParam("quantity") int quantity,
            @RequestParam("description") String description,
            @RequestParam("productImage") String imageUrl) {

        LOGGER.log(Level.INFO, "Adding product under category: {0}", categoryId);
        ProductGroup group = this.categoryService.findCategoryById(categoryId);

        Merchandise item = new Merchandise();
        item.setName(name);
        item.setCategory(group);
        item.setDescription(description);
        item.setPrice(price);
        item.setImage(imageUrl);
        item.setWeight(weight);
        item.setQuantity(quantity);
        this.catalogService.createProduct(item);

        return "redirect:/admin/products";
    }

    @GetMapping("products/update/{id}")
    public ModelAndView showUpdateProductForm(@PathVariable("id") int id) {
        ModelAndView view = new ModelAndView("productsUpdate");
        Merchandise item = this.catalogService.findProductById(id);
        List<ProductGroup> groups = this.categoryService.listAllCategories();
        view.addObject("categories", groups);
        view.addObject("product", item);
        return view;
    }

    @RequestMapping(value = "products/update/{id}", method = RequestMethod.POST)
    public String handleProductUpdate(
            @PathVariable("id") int id,
            @RequestParam("name") String name,
            @RequestParam("categoryid") int categoryId,
            @RequestParam("price") int price,
            @RequestParam("weight") int weight,
            @RequestParam("quantity") int quantity,
            @RequestParam("description") String description,
            @RequestParam("productImage") String imageUrl) {
        return "redirect:/admin/products";
    }

    @GetMapping("products/delete")
    public String deleteProduct(@RequestParam("id") int id) {
        this.catalogService.removeProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("products")
    public String handleProductPost() {
        return "redirect:/admin/categories";
    }

    // --- Customer management ---

    @GetMapping("customers")
    public ModelAndView showCustomerDirectory() {
        ModelAndView view = new ModelAndView("displayCustomers");
        List<Customer> customers = this.accountService.getAllCustomers();
        view.addObject("customers", customers);
        return view;
    }

    // --- Profile management ---

    @GetMapping("profileDisplay")
    public String showAdminProfile(Model model) {
        String currentUser = resolveCurrentUsername();
        Customer customer = this.accountService.findByUsername(currentUser);

        if (customer != null) {
            model.addAttribute("userid", customer.getId());
            model.addAttribute("username", customer.getUsername());
            model.addAttribute("email", customer.getEmail());
            model.addAttribute("password", customer.getPassword());
            model.addAttribute("address", customer.getAddress());
        }
        return "updateProfile";
    }

    @RequestMapping(value = "updateuser", method = RequestMethod.POST)
    public String handleProfileUpdate(
            @RequestParam("userid") int userId,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("address") String address) {

        try {
            this.accountService.updateProfile(userId, username, email, password, address);

            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
            Authentication refreshedAuth = new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    currentAuth.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(refreshedAuth);

            LOGGER.info("Profile updated successfully for user: " + username);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Profile update failed: {0}", ex.getMessage());
        }
        return "redirect:index";
    }

    private String resolveCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

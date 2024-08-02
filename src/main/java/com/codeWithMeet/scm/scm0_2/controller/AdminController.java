package com.codeWithMeet.scm.scm0_2.controller;

import com.codeWithMeet.scm.scm0_2.Service.ImgService;
import com.codeWithMeet.scm.scm0_2.Service.ProductService;
import com.codeWithMeet.scm.scm0_2.Service.UserService;
import com.codeWithMeet.scm.scm0_2.entities.Product;
import com.codeWithMeet.scm.scm0_2.entities.User;
import com.codeWithMeet.scm.scm0_2.forms.ProductForm;
import com.codeWithMeet.scm.scm0_2.helper.Helper;
import com.codeWithMeet.scm.scm0_2.helper.MessageHelper;
import com.codeWithMeet.scm.scm0_2.helper.MessageType;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/")
public class AdminController {


    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ImgService imgService;


    //    admin Dashboard page
    @PostMapping("/dashboard")
    public String adminDashboard(Model model, Authentication authentication) {
        System.out.println(" admin desboared her ");
        return "admin/dashboard";
    }

    //    user Dashboard page
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }


    @GetMapping("product/add")
    public String addContactView(Model model, ProductForm productForm, BindingResult result, HttpSession session) {
        System.out.println("********* Get request in contact form ********");
        model.addAttribute("product", productForm);

        if (result.hasErrors()) {
            session.setAttribute("message", MessageHelper.builder().content("Please correct the following errors").type(MessageType.red).build());
            model.addAttribute("product", productForm);
        }

        return "admin/add_product";
    }

    @PostMapping("product/add")
    public String savecontact(@Valid ProductForm productForm, BindingResult result, Authentication authentication, Model model, HttpSession session) {
        System.out.println("********* post request in product form ********");


        if (result.hasErrors()) {
            session.setAttribute("message", MessageHelper.builder().content("Please correct the following errors").type(MessageType.red).build());
            System.out.println("data nathi malto ");
            model.addAttribute("product", productForm);
            return "user/add_product";
        }


        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Product product = getProduct(productForm, user);


        System.out.println(" file name ::------ " + productForm.getProductImage().getOriginalFilename());

        if (productForm.getProductImage().getOriginalFilename() == null) {
            System.out.println("file j nathi aavti ");
        }

        String fileurl = imgService.UploadIMG(productForm.getProductImage());
        System.out.println(" file url " + fileurl);
        product.setProductImage(fileurl);
        productService.save(product);


        session.setAttribute("message", MessageHelper.builder().content("your product added successfully").type(MessageType.green).build());
        model.addAttribute("product", product);

        return "admin/add_product";

    }

    private static Product getProduct(ProductForm productForm, User user) {
        Product product = new Product();


        product.setName(productForm.getName());
        product.setFavorite(productForm.getFavorite());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setUser(user);


        return product;
    }


}

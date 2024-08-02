package com.codeWithMeet.scm.scm0_2.controller;

import com.codeWithMeet.scm.scm0_2.Repo.CartRepo;
import com.codeWithMeet.scm.scm0_2.Service.CartService;
import com.codeWithMeet.scm.scm0_2.Service.ProductOrderService;
import com.codeWithMeet.scm.scm0_2.Service.UserService;
import com.codeWithMeet.scm.scm0_2.entities.Cart;
import com.codeWithMeet.scm.scm0_2.entities.User;
import com.codeWithMeet.scm.scm0_2.forms.OrderRequestForm;
import com.codeWithMeet.scm.scm0_2.helper.Helper;
import com.codeWithMeet.scm.scm0_2.Repo.ProductOrderRepo;
import com.codeWithMeet.scm.scm0_2.entities.ProductOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("user/product/order")
public class OrderController {


    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductOrderRepo productOrderRepo;

    @Autowired
    private CartRepo cartRepo;


    @GetMapping("/")
    public String orderpage(Authentication authentication, Model model) {


        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        List<Cart> carts = cartService.getCartByUser(user.getUserid());

        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            double orderamount = ((carts.get(carts.size() - 1)).getTotalOrderAmount());
            double gst = ((carts.get(carts.size() - 1)).getTotalOrderAmount()) * 0.02;
            double finalamount = orderamount + gst;

            model.addAttribute("orderamount", orderamount);
            model.addAttribute("gst", (int) gst);
            model.addAttribute("finalamount", finalamount);


        }
        return "user/orderpage";
    }

    @PostMapping("/save-order")
    public String saveorder(@ModelAttribute OrderRequestForm request, Authentication authentication) {

        System.out.println("saave user page ........");
        System.out.println(request + " 555555555555555");

        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        productOrderService.saveorder(user.getUserid(), request);

        List<Cart> carts = cartService.getCartByUser(user.getUserid());

        for (Cart cart : carts) {
            cartRepo.deleteById(cart.getId());
        }

        return "redirect:/user/product/order/orderSuccesspage";

    }

    @GetMapping("/orderSuccesspage")
    public String myCart(Authentication authentication, Model model) {


        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        
        List<ProductOrder> order = productOrderRepo.findByUserUserid(user.getUserid());
        List<ProductOrder> order1 = productOrderRepo.findByUserId(user.getUserid(), order.get(order.size() - 1).getOrderId());

//        cartService.DeleteCart(user.getUserid());


//        System.out.println(order + " ////////////// ");

        model.addAttribute("order", order1);
        model.addAttribute("order1", order1);
        model.addAttribute("orderid", order.get(order.size() - 1).getOrderId());



        return "user/orderSuccesspage";
    }



    @GetMapping("/my-order")
    public  String myorder(Authentication authentication, Model model) {

        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);



        List<ProductOrder> order = productOrderRepo.findByUserUserid(user.getUserid());
        List<ProductOrder> userOrder = productOrderRepo.findByUserId(user.getUserid(), order.get(order.size() - 1).getOrderId());

        model.addAttribute("order", order);
        model.addAttribute("userOrder", userOrder);

        return "user/my-order";
    } 



}

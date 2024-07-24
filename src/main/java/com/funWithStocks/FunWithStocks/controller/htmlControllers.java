package com.funWithStocks.FunWithStocks.controller;

import com.funWithStocks.FunWithStocks.component.JwtTokenUtil;
import com.funWithStocks.FunWithStocks.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class htmlControllers {

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/admin")
    public String adminPage() {
        return "demoWebSocket";
    }


    @GetMapping("/index")
    public String hello() {
        return "homepage";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        return "signup";
    }

    @GetMapping("/depositPage")
    public String depositPage(Model model) {
        return "deposit";
    }

    @GetMapping("/paymentPage")
    public String payPage(Model model) {
        return "payment";
    }

    @GetMapping("/withdraw")
    public String withdrawPage(Model model) {
        return "withdraw";
    }

    @GetMapping("/results")
    public String results() {
        return "results";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam (name = "logout", required = false) String logout) {
        return "login";
    }

    @GetMapping("/profile")
    public String profileDetails(Model model) {
            return "profile";
    }

    @PostMapping("/logout")
    public String logout(Model model) {
        // Perform logout operation
        SecurityContextHolder.clearContext();
        // Redirect to the login page
        return "redirect:/login?logout";
    }

    @GetMapping("/members")
    public String memberMatrix() {
        return "Redirect2Tree";
    }

    @GetMapping("/showMembers")
    public String redirect2TreePage() {
        return "newtree";
    }

    @GetMapping("/viewTransactions")
    public String viewTransactions(Model model) {
        return "fetchTransactions";
    }

    @GetMapping("/api/v1/userDashboard")
    public String dashboard() {
        return "numberselect";
    }

    @GetMapping("/redirectToDashBoard")
    public String redirectFromPayment() {
        return "redirectPaymentPage";
    }
}

package com.demo.sqlite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

   @GetMapping("/")
   public String redirectToHomePage() {
      return "redirect:/swagger-ui/index.html";
   }

}

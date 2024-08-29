package com.demo.sqlite.controller;

import com.demo.sqlite.model.entity.Category;
import com.demo.sqlite.service.LookupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/lookup")
public class LookupController {

   private final LookupService lookupService;

   public LookupController(@Autowired LookupService lookupService) {
      this.lookupService = lookupService;
   }

   @GetMapping(path = "/categories")
   @Operation(summary = "List categories")
   public @ResponseBody Iterable<Category> getCategories() {
      return lookupService.allCategories();
   }

}

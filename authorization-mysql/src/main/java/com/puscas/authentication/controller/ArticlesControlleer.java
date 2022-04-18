package com.puscas.authentication.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/item")
public class ArticlesControlleer {

    @GetMapping("/articles")
    public String[] getArticles() {
        return new String[] { "Article 1", "Article 2", "Article 3" };
    }


}

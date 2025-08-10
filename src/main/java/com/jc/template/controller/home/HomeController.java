package com.jc.template.controller.home;

import com.jc.template.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController extends BaseController {
    public HomeController(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

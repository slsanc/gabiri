package slsanc.gabiri.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {

    @GetMapping("")
    public String gethomepage(Model model){
        return "index";
    }

}

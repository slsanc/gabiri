package slsanc.gabiri.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping("hiring")
public class TestController {

    @RequestMapping(value = "")
    public String index(Model model) {

        return "hiring/index";
    }

}

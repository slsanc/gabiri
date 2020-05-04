package slsanc.gabiri.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import slsanc.gabiri.models.Position;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("hiring")
public class HiringController {

    public ArrayList<Position> positionsList = new ArrayList<>();

    @RequestMapping (value = "")
    public String index(Model model) {
        model.addAttribute("positionsList", positionsList);
        return "hiring/index";
    }

    @GetMapping("newopenposition")
    public String NewPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "hiring/newopenposition";
    }

    @PostMapping("newopenposition")
    public String NewPositionForm(@ModelAttribute Position position) {
        positionsList.add(position);
        return "redirect:";
    }


}

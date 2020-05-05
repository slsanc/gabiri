package slsanc.gabiri.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import slsanc.gabiri.data.PositionRepository;
import slsanc.gabiri.models.Position;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("hiring")
public class HiringController {

    @Autowired
    private PositionRepository positionRepository;

    @RequestMapping (value = "")
    public String index(Model model) {
        model.addAttribute("positionsList", positionRepository.findAll());
        return "hiring/hiring";
    }

    @GetMapping("newopenposition")
    public String NewPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "hiring/newopenposition";
    }

    @PostMapping("newopenposition")
    public String NewPositionForm(@ModelAttribute Position position) {
        positionRepository.save(position);
        return "redirect:";
    }


}

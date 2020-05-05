package slsanc.gabiri.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import slsanc.gabiri.data.ApplicantRepository;
import slsanc.gabiri.data.PositionRepository;
import slsanc.gabiri.models.Applicant;
import slsanc.gabiri.models.Position;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("hiring")
public class HiringController {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @GetMapping (value = "positions")
    public String displayPositions(Model model) {
        model.addAttribute("positionsList", positionRepository.findAll());
        return "hiring/positions";
    }

    @GetMapping("newopenposition")
    public String displayNewPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "hiring/newopenposition";
    }

    @PostMapping("newopenposition")
    public String processNewPositionForm(@ModelAttribute Position position) {
        positionRepository.save(position);
        return "redirect:";
    }

    @GetMapping (value = "applicants")
    public String displayApplicants(Model model) {
        model.addAttribute("applicantsList", applicantRepository.findAll());
        return "hiring/applicants";
    }

    @GetMapping("newapplicant")
    public String displayNewApplicantForm(Model model) {
        model.addAttribute("applicant", new Applicant());
        return "hiring/newapplicant";
    }

    @PostMapping("newapplicant")
    public String processNewApplicantForm(@ModelAttribute Applicant applicant) {
        applicantRepository.save(applicant);
        return "redirect:/hiring/applicants";
    }

}

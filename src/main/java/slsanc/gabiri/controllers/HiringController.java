package slsanc.gabiri.controllers;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import slsanc.gabiri.data.ApplicantRepository;
import slsanc.gabiri.data.ApplicationRepository;
import slsanc.gabiri.data.PositionRepository;
import slsanc.gabiri.models.Applicant;
import slsanc.gabiri.models.Application;
import slsanc.gabiri.models.IdList;
import slsanc.gabiri.models.Position;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("hiring")
public class HiringController {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicationRepository applicationRepository;


    @GetMapping (value = "")
    public String displayIndex(Model model) {
        return "hiring/hiring";
    }


    @GetMapping("newopenposition")
    public String displayNewPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "hiring/newopenposition";
    }


    @PostMapping("newopenposition")
    public String processNewPositionForm(@ModelAttribute Position position) {
        positionRepository.save(position);
        return "redirect:/hiring/positions";
    }


    @GetMapping (value = "positions")
    public String displayPositions(Model model) {
        model.addAttribute("positionsList", positionRepository.findAll());
        return "hiring/positions";
    }


    @PostMapping (value = "positions")
    /* This bit handles when the user clicks on a position in the list of positions. It shows them a page containing
    information about that particular position, as well as a list of applicants who have applied to that position.*/
    public String processPositions(HttpServletRequest httpServletRequest, Model model) {

        int positionId = Integer.parseInt(httpServletRequest.getParameter("positionId"));

        model.addAttribute("position",positionRepository.findById(positionId).get());

        return "hiring/viewposition";
    }


    @PostMapping (value = "considernewapplicants")
    /* This bit handles when the user clicks "consider new applicants for this position"
    while viewing an open position's page.*/
    public String processConsiderNewApplicants(HttpServletRequest httpServletRequest, Model model) {

        /*This line sets idList.id to the ID of the position that the list of applicants is being selected for.*/
        IdList idList = new IdList(Integer.parseInt(httpServletRequest.getParameter("positionId")));


        model.addAttribute("applicantIterable",applicantRepository.findAll());
        model.addAttribute("idList",idList);

        return "hiring/considernewapplicants";
    }


    @PostMapping (value = "addapplicanttoposition")
    /* This bit handles when the user clicks "add selected applicants to position" */
    public String processAddApplicantToPosition(@ModelAttribute IdList idList){

        /* The following code creates an object of class Application. It specifies that it is an application
        * a particular open position chosen by the user. Then, it goes through a list of applicants that the user
        * wants to consider for this position. It creates an entry in the table of applications for each one,
        * recording that each person on the list has now applied for the given position.
        * It also records the date on which this was done and sets the application status to '1'
        * (meaning "Under Consideration").*/

        Application application = new Application();
        application.setPositionId(idList.getId());

        for (Integer i : idList.getIdList())
        {
            application.setApplicantId(i.intValue());
            application.setDateApplied(Date.valueOf(LocalDate.now()));
            application.setStatus(1);
            applicationRepository.save(application);
        }

        return "redirect:/hiring/positions";
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

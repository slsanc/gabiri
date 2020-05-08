package slsanc.gabiri.controllers;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
        /* this next line feeds a list of open positions into the model. */
        model.addAttribute("positionsList", positionRepository.openPositions());
        return "hiring/positions";
    }


    @PostMapping (value = "positions")
    /* This bit handles when the user clicks on a position in the list of positions. It shows them a page containing
    information about that particular position, as well as a list of applicants who have applied to that position.*/
    public String processPositions(HttpServletRequest httpServletRequest, Model model) {

        int positionId = Integer.parseInt(httpServletRequest.getParameter("positionId"));

        model.addAttribute("applicantsForThisPosition",
                applicantRepository.findApplicantsByPositionAppliedfor(positionId));
        model.addAttribute("position",positionRepository.findById(positionId).get());
        model.addAttribute("application",new Application());

        return "hiring/viewposition";
    }


    @PostMapping (value = "considernewapplicants")
    /* This bit handles when the user clicks "consider new applicants for this position"
    while viewing an open position's page.*/
    public String processConsiderNewApplicants(HttpServletRequest httpServletRequest, Model model) {

        int positionId = Integer.parseInt(httpServletRequest.getParameter("positionId"));
        model.addAttribute("applicantsNotYetConsidered", applicantRepository.applicantsNotYetConsidered(positionId));
        model.addAttribute("idList", new IdList(positionId));

        return "hiring/considernewapplicants";
    }


    @PostMapping (value = "addapplicanttoposition")
    /* This bit handles when the user clicks "add selected applicants to position" */
    public String processAddApplicantToPosition(@ModelAttribute IdList idList){

        /* The following code takes a list of users selected by the user, as well as a positionID the user has
        selected, and records in the "Applications" table that each applicant has now applied for the given position.*/

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

    @PostMapping (value = "fillposition")
    public String fillposition(@ModelAttribute Application application){

        positionRepository.fillPosition(application.getPositionId() , Date.valueOf(LocalDate.now()));
        applicationRepository.changeStatus(application.getApplicantId() , application.getPositionId() , 3);

        return "redirect:/hiring/positions";

    }


    @GetMapping (value = "filledpositions")
    public String displayFilledPositions(Model model){

        HashMap<Position , Applicant> hashMap = new HashMap<Position , Applicant>();
        Applicant applicant;

        for (Position position : positionRepository.filledPositions())
        {
            applicant = applicantRepository.applicantHiredFor(position.getPositionId());
            hashMap.put(position,applicant);
        }

        model.addAttribute("hashMap",hashMap);

        return "hiring/filledpositions";
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

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
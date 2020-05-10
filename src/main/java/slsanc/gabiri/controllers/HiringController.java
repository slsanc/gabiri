package slsanc.gabiri.controllers;

import org.apache.tomcat.jni.Local;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import slsanc.gabiri.data.ApplicantRepository;
import slsanc.gabiri.data.ApplicationRepository;
import slsanc.gabiri.data.DocumentRepository;
import slsanc.gabiri.data.PositionRepository;
import slsanc.gabiri.models.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Controller
public class HiringController {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private DocumentRepository documentRepository;


    @GetMapping ("")
    public String displayIndex(Model model) {
        return "hiring/index";
    }


    @GetMapping("newopenposition")
    public String displayNewPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "hiring/newopenposition";
    }


    @PostMapping("newopenposition")
    public String processNewPositionForm(@ModelAttribute Position position) {
        positionRepository.save(position);
        return "redirect:/openpositions";
    }


    @GetMapping ("openpositions")
    public String displayPositions(Model model) {
        /* this next line feeds a list of open positions into the model. */
        model.addAttribute("positionsList", positionRepository.openPositions());
        return "hiring/openpositions";
    }


    @GetMapping ("/viewposition/{positionId}")
    public String processPositions(@PathVariable("positionId") int positionId, Model model) {

        model.addAttribute("applicantsForThisPosition",
                applicantRepository.findApplicantsByPositionAppliedfor(positionId));
        model.addAttribute("position",positionRepository.findById(positionId).get());
        model.addAttribute("application",new Application());

        return "hiring/viewposition";
    }

    @PostMapping ("deleteposition")
    public String deleteposition(@RequestParam int positionId){
        positionRepository.deleteById(positionId);
        return "redirect:/openpositions";
    }

    @GetMapping ("considernewapplicants/{positionId}")
    /* This bit handles when the user clicks "consider new applicants for this position"
    while viewing an open position's page.*/
    public String processConsiderNewApplicants(@PathVariable int positionId, Model model) {

        model.addAttribute("applicantsNotYetConsidered", applicantRepository.applicantsNotYetConsidered(positionId));
        model.addAttribute("idList", new IdList(positionId));

        return "hiring/considernewapplicants";
    }


    @PostMapping ("addapplicanttoposition")
    /* This bit handles when the user clicks "add selected applicants to position" */
    public String processAddApplicantToPosition(@ModelAttribute IdList idList){

        /* The following code takes a list of users selected by the user, as well as a positionID the user has
        selected, and records in the "Applications" table that each applicant has now applied for the given position.*/

        Application application = new Application();
        application.setPositionId(idList.getId());

        for (Integer i : idList.getIdList())
        {
            application.setApplicantId(i);
            application.setDateApplied(Date.valueOf(LocalDate.now()));
            application.setStatus(1);
            applicationRepository.save(application);
        }

        return "redirect:/viewposition/" + application.getPositionId();
    }

    @PostMapping ("fillposition")
    public String fillposition(@ModelAttribute Application application){

        positionRepository.setDateFilled(application.getPositionId() , Date.valueOf(LocalDate.now()));
        applicationRepository.changeStatus(application.getApplicantId() , application.getPositionId() , 3);
        applicationRepository.rejectRunnersUp(application.getPositionId() , application.getApplicantId());

        return "redirect:/openpositions";
    }


    @GetMapping ("filledpositions")
    public String displayFilledPositions(Model model){

        HashMap<Position , Applicant> hashMap = new HashMap<>();
        Applicant applicant;

        for (Position position : positionRepository.filledPositions())
        {
            System.out.println(position.getPositionId());
            applicant = applicantRepository.applicantHiredFor(position.getPositionId());
            hashMap.put(position,applicant);
        }

        model.addAttribute("hashMap",hashMap);

        return "hiring/filledpositions";
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


    @GetMapping("newapplicant")
    public String displayNewApplicantForm(Model model) {
        model.addAttribute("applicant", new Applicant());
        return "hiring/newapplicant";
    }


    @PostMapping("newapplicant")
    public String processNewApplicantForm(@ModelAttribute Applicant applicant,
                                          @RequestParam MultipartFile[] files) {
        applicantRepository.save(applicant);

        for(MultipartFile file:files) {
            try {
                if (!(file.isEmpty())) {
                    Document document = new Document(applicant.getApplicantId(),
                            file.getOriginalFilename(), file.getBytes());
                    documentRepository.save(document);
                }
            } catch (IOException e) {
                return "redirect:/";
            }
        }
        return "redirect:/availableapplicants";
    }


    @GetMapping ("availableapplicants")
    public String displayApplicants(Model model) {
        model.addAttribute("applicantsList", applicantRepository.availableApplicants());
        return "hiring/availableapplicants";
    }


    @GetMapping ("viewapplicant/{applicantId}")
    public String processApplicants(@PathVariable("applicantId") int applicantId, Model model){

        model.addAttribute("applicant" , applicantRepository.findById(applicantId).get());
        model.addAttribute("positionsList"
                , positionRepository.OpenPositionsThisApplicantHasAppliedFor(applicantId));
        model.addAttribute("documentIdsAndNames"
                , documentRepository.thisApplicantsDocumentIdsAndNames(applicantId));

        return "/hiring/viewapplicant";
    }

    @PostMapping ("deleteapplicant")
    public String deleteApplicant(@RequestParam int applicantId){

        positionRepository.deletePositionsThisApplicantWasHiredFor(applicantId);

        applicantRepository.deleteById(applicantId);
        return "redirect:/availableapplicants";
    }

    @PostMapping("uploaddocuments")
    public String uploadDocuments(@RequestParam String applicantId , @RequestParam MultipartFile[] files) {

        for(MultipartFile file:files) {
            try {
                if (!(file.isEmpty())) {
                    Document document = new Document( Integer.parseInt(applicantId),
                            file.getOriginalFilename(), file.getBytes());
                    documentRepository.save(document);
                }
            } catch (IOException e) {
                return "redirect:/hiring";
            }
        }
        return "redirect:/success/" + applicantId;
    }

    @PostMapping ("downloaddocument")
    public ResponseEntity downloadDocument(@RequestParam String documentId) {

        Document document = documentRepository.findById(Integer.parseInt(documentId)).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getFileName() + "\"")
                .body(document.getDocumentData());
    }

    @PostMapping ("deletedocument")
    public String deleteDocument(@RequestParam String documentId , @RequestParam String applicantId , Model model)
    {
        documentRepository.deleteById(Integer.parseInt(documentId));

        return "redirect:/success/" + applicantId;
    }

    @GetMapping ("success/{applicantId}")
    public String success(@PathVariable String applicantId , Model model)
    {
        model.addAttribute("applicantId", applicantId);
        return "/hiring/success";
    }
}
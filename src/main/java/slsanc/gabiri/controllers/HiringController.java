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
        Position position = positionRepository.findById(positionId).get();
        HashMap<Applicant , Application> applicantsApplicationsHashMap = new HashMap<>();

        /*The following loop goes through a list of applications to this position and finds the associated applicant.
        * It returns the two, linked in a hashmap.*/
        for(Application a : applicationRepository.findApplicationsByPosition(positionId , position.isStillOpen())){
            applicantsApplicationsHashMap.put(applicantRepository.findById(a.getApplicantId()).get() , a);
        }

        model.addAttribute("applicantsApplicationsHashMap", applicantsApplicationsHashMap);
        model.addAttribute("position",position);
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
    public String AddApplicantToPosition(@ModelAttribute IdList idList, @RequestParam Date dateApplied){



        Application application = new Application();
        application.setPositionId(idList.getId());

        if(dateApplied == null)
        {
            application.setDateApplied(Date.valueOf(LocalDate.now()));
        }
        else
        {
            application.setDateApplied(dateApplied);
        }

        /* The following code takes a list of users selected by the user, as well as a positionID the user has
        selected, and records in the "Applications" table that each applicant has now applied for the given position.*/
        for (Integer i : idList.getIdList())
        {
            application.setApplicantId(i);
            application.setStatus(1);
            applicationRepository.save(application);
        }

        return "redirect:/viewposition/" + application.getPositionId();
    }

    @PostMapping ("changeconsiderationstatus")
    public String changeConsiderationStatus(@RequestParam int applicantIdToReconsider
            , @RequestParam int positionId){

        Application application = applicationRepository.findApplicationByPositionAndApplicant(
                positionId, applicantIdToReconsider);

        if (application.getStatus() == 1){
            application.setStatus(2);
        }
        else if (application.getStatus() == 2){
            application.setStatus(1);
        }

        applicationRepository.changeStatus(application.getPositionId() , application.getApplicantId()
                , application.getStatus());

        return "redirect:/success/position/" + positionId;
    }

    @PostMapping ("fillposition")
    public String fillPosition(@ModelAttribute Application application){

        positionRepository.setDateFilled(application.getPositionId() , Date.valueOf(LocalDate.now()));
        applicationRepository.changeStatus(application.getPositionId() , application.getApplicantId() , 3);
        applicationRepository.rejectRunnersUp(application.getPositionId() , application.getApplicantId());

        return "redirect:/openpositions";
    }


    @GetMapping ("filledpositions")
    public String displayFilledPositions(Model model){

        HashMap<Position , Applicant> positionApplicantHashMap = new HashMap<>();
        Applicant applicant;

        for (Position position : positionRepository.filledPositions())
        {
            applicant = applicantRepository.applicantHiredFor(position.getPositionId());
            positionApplicantHashMap.put(position,applicant);
        }

        model.addAttribute("positionApplicantHashMap",positionApplicantHashMap);

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

        HashMap<Position,Application>positionApplicationHashMap = new HashMap<>();

        for(Position position: positionRepository.positionsAppliedTo(applicantId)){
            positionApplicationHashMap.put(position, applicationRepository.findApplicationByPositionAndApplicant
                    (position.getPositionId() , applicantId));
        }


        model.addAttribute("applicant" , applicantRepository.findById(applicantId).get());
        model.addAttribute("positionApplicationHashMap", positionApplicationHashMap);
        model.addAttribute("documentIdsAndNames"
                , documentRepository.thisApplicantsDocumentIdsAndNames(applicantId));

        return "/hiring/viewapplicant";
    }

    @GetMapping ("considernewpositions/{applicantId}")
    public String considerNewPositions(@PathVariable int applicantId , Model model){
        IdList idList = new IdList(applicantId);
        model.addAttribute("idList" , idList);
        model.addAttribute("positionsNotYetAppliedTo" , positionRepository.positionsNotYetAppliedTo(applicantId));
        return "/hiring/considernewpositions";
    }

    @PostMapping ("addpositiontoapplicant")
    public String addPositionToApplicant(@ModelAttribute IdList idList , @RequestParam Date dateApplied){

        Application application = new Application();
        application.setApplicantId(idList.getId());

        if(dateApplied == null)
        {
            application.setDateApplied(Date.valueOf(LocalDate.now()));
        }
        else
        {
            application.setDateApplied(dateApplied);
        }

        for (Integer i : idList.getIdList())
        {
            application.setPositionId(i);
            application.setStatus(1);
            applicationRepository.save(application);
        }

        return "redirect:/viewapplicant/" + application.getApplicantId();
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
                return "redirect:/";
            }
        }
        return "redirect:/success/applicant/" + applicantId;
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
        return "redirect:/success/applicant/" + applicantId;
    }

    @GetMapping ("success/{positionOrApplicant}/{id}")
    public String success(@PathVariable String positionOrApplicant , @PathVariable String id, Model model)
    {
        model.addAttribute("positionOrApplicant", positionOrApplicant);
        model.addAttribute("id", id);
        return "/hiring/success";
    }

    @GetMapping ("errorpage")
    public String errorpage(){
        return "/hiring/errorpage";
    }
}
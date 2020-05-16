package slsanc.gabiri.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import slsanc.gabiri.data.*;
import slsanc.gabiri.models.*;
import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
public class HiringController {

    //<editor-fold desc="Creation of repository objects">
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    //</editor-fold>

    //<editor-fold desc="Success, Errors and home">
    @GetMapping ("")
    public String displayIndex(Model model, Principal principal) {
        model.addAttribute("firstName", userRepository.findUserByUsername(principal.getName()).getFirstName());
        return "hiring/index";
    }

    @GetMapping ("success/{positionOrApplicant}/{id}")
    public String success(@PathVariable String positionOrApplicant , @PathVariable String id, Model model) {
        model.addAttribute("positionOrApplicant", positionOrApplicant);
        model.addAttribute("id", id);
        return "/hiring/success";
    }

    @GetMapping ("errorpage")
    public String errorpage(){
        return "/hiring/errorpage";
    }


    //</editor-fold>

    //<editor-fold desc="Methods to do with handling positions">
    @GetMapping("newopenposition")
    public String displayNewPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "hiring/newopenposition";
    }


    @PostMapping("newopenposition")
    public String processNewPositionForm(@ModelAttribute Position position, Principal principal) {
        position.setOwnerId(userRepository.findUserIdByUsername(principal.getName()));
        positionRepository.save(position);
        return "redirect:/openpositions";
    }


    @GetMapping ("openpositions")
    public String displayPositions(Model model, Principal principal) {


        /*if a user isn't a super-user, only show them open positions they created. But if they are a super-user, show
        * them every position in the database:*/

        if(userRepository.findRoleIdByUsername(principal.getName()) != 1){
            model.addAttribute("positionsList"
                    , positionRepository.openPositionsThisUserCreated(principal.getName()));
        }
        else{
            model.addAttribute("positionsList", positionRepository.allOpenPositions());
        }

        return "hiring/openpositions";
    }


    @GetMapping ("/viewposition/{positionId}")
    public String processPositions(@PathVariable("positionId") int positionId, Model model, Principal principal) {
        if(positionRepository.userOwnsThisPosition(principal.getName(), positionId) == 1){
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
        else{
            return "redirect:/openpositions";
        }

    }

    @PostMapping ("deleteposition")
    public String deleteposition(@RequestParam int positionId){
        positionRepository.deleteById(positionId);
        return "redirect:/openpositions";
    }

    @GetMapping ("considernewapplicants/{positionId}")
    /* This bit handles when the user clicks "consider new applicants for this position"
    while viewing an open position's page.*/
    public String processConsiderNewApplicants(@PathVariable int positionId, Model model, Principal principal) {

        if(positionRepository.userOwnsThisPosition(principal.getName(), positionId) == 1){
            model.addAttribute("applicantsNotYetConsidered"
                    , applicantRepository.applicantsNotYetConsidered(positionId));
            model.addAttribute("idList", new IdList(positionId));

            return "hiring/considernewapplicants";
        }
        else{
            return "redirect:/openpositions";
        }
    }

    @PostMapping ("addapplicanttoposition")
    /* This bit handles when the user clicks "add selected applicants to position" */
    public String AddApplicantToPosition(@ModelAttribute IdList idList, @RequestParam Date dateApplied){



        Application application = new Application();
        application.setPositionId(idList.getId());

        application.setDateApplied(Objects.requireNonNullElseGet(dateApplied, () -> Date.valueOf(LocalDate.now())));

//        if(dateApplied == null)
//        {
//            application.setDateApplied(Date.valueOf(LocalDate.now()));
//        }
//        else
//        {
//            application.setDateApplied(dateApplied);
//        }

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
    public String displayFilledPositions(Model model , Principal principal){

        List<Position> filledPositionsList;
        Applicant applicant;
        HashMap<Position , Applicant> positionApplicantHashMap = new HashMap<>();

        /*if a user isn't a super-user, only show them filled positions they created. But if they are a super-user, show
         * them every filled position in the database:*/

        if(userRepository.findRoleIdByUsername(principal.getName()) != 1)
        {
            filledPositionsList = positionRepository.filledPositionsThisUserCreated(principal.getName());
        }
        else {
            filledPositionsList = positionRepository.allFilledPositions();
        }

        /*Create a HashMap where each position is connected to the applicant who was hired for it:*/
        for (Position position : filledPositionsList)
        {
            positionApplicantHashMap.put(position, applicantRepository.applicantHiredFor(position.getPositionId()));
        }

        model.addAttribute("positionApplicantHashMap",positionApplicantHashMap);

        return "hiring/filledpositions";
    }
    //</editor-fold>

    //<editor-fold desc="Methods to do with handling applicants">
    @GetMapping("newapplicant")
    public String displayNewApplicantForm(Model model) {
        model.addAttribute("applicant", new Applicant());
        return "hiring/newapplicant";
    }


    @PostMapping("newapplicant")
    public String processNewApplicantForm(@ModelAttribute Applicant applicant, @RequestParam MultipartFile[] files
            , Principal principal) {

        applicant.setOwnerId(userRepository.findUserIdByUsername(principal.getName()));
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
    public String displayApplicants(Model model, Principal principal) {

        List<Applicant> applicantsList;

        if(userRepository.findRoleIdByUsername(principal.getName()) != 1)
        {
            applicantsList = applicantRepository.availableApplicantsThisUserCreated(principal.getName());
        }
        else {
            applicantsList = applicantRepository.allAvailableApplicants();
        }

        model.addAttribute("applicantsList", applicantsList);
        return "hiring/availableapplicants";
    }


    @GetMapping ("viewapplicant/{applicantId}")
    public String processApplicants(@PathVariable("applicantId") int applicantId, Model model, Principal principal){

        if(applicantRepository.userOwnsThisApplicant(principal.getName(), applicantId) == 1){
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
        else{
            return "redirect:/availableapplicants";
        }

    }

    @GetMapping ("considernewpositions/{applicantId}")
    public String considerNewPositions(@PathVariable int applicantId , Model model, Principal principal){
        if(applicantRepository.userOwnsThisApplicant(principal.getName(), applicantId) == 1){
            IdList idList = new IdList(applicantId);
            model.addAttribute("idList" , idList);
            model.addAttribute("positionsNotYetAppliedTo" , positionRepository.positionsNotYetAppliedTo(applicantId));
            return "/hiring/considernewpositions";
        }
        else{
            return "redirect:/availableapplicants";
        }
    }

    @PostMapping ("addpositiontoapplicant")
    public String addPositionToApplicant(@ModelAttribute IdList idList , @RequestParam Date dateApplied){

        Application application = new Application();
        application.setApplicantId(idList.getId());

        application.setDateApplied(Objects.requireNonNullElseGet(dateApplied, () -> Date.valueOf(LocalDate.now())));

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
    //</editor-fold>



}
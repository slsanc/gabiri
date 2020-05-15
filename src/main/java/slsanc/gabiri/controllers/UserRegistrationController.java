package slsanc.gabiri.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import slsanc.gabiri.data.UserRepository;
import slsanc.gabiri.models.User;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/login")
    public String login(){
        return "/registration/login";
    }

    @GetMapping("")
    public String displayResgistrationForm(Model model) {
        model.addAttribute("newUser", new User());
        return "/registration/registrationform";
    }

    @PostMapping("")
    public RedirectView processResgistrationForm(@Valid User newUser
            , @RequestParam String confirmPassword
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes) {

        String viewName = "/registration/success";
        String message = new String();

        System.out.println(newUser.getPassword());
        System.out.println(confirmPassword);

        if (userRepository.findUserByEmail(newUser.getEmail()) != null) {
            message = "A user with this email already exists!";
            viewName = "/registration/error";
        }
        else if (userRepository.findUserByUsername(newUser.getUsername()) != null) {
            message = "A user with this email already exists!";
            viewName = "/registration/error";
        }
        else if (!(newUser.getPassword().equals(confirmPassword))) {
            message = "The given passwords did not match!";
            viewName = "/registration/error";
        }
        else {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            userRepository.save(newUser);
            userRepository.createPermissions(newUser.getUserId() , 3);
            viewName = "/registration/success";
        }

        redirectAttributes.addFlashAttribute("message",message);
        return new RedirectView(viewName);
    }

    @GetMapping("/success")
    public String registrationSuccess () {
        return "/registration/success";
    }

    @GetMapping("/error")
    public String registrationError () {
        return "/registration/error";
    }

}

package com.bohype.controller;

import com.bohype.bean.CredentialBean;
import com.bohype.service.BoUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final BoUserDetailService boUserDetailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder =new BCryptPasswordEncoder();
    @GetMapping("/home")
    public String index(){
        return "home";
    }

    @GetMapping("/index")
    public String login(HttpServletRequest request, Model model){
        var error = request.getParameter("error");
        if (null != error) {
            model.addAttribute("loginError", true);
        }
        model.addAttribute("credential",new CredentialBean());
        return "index";
    }

    @PostMapping("/pre-login")
    public String preCheckLogin(@ModelAttribute CredentialBean credentialBean){
        final var userDetails = boUserDetailService.loadUserByUsername(credentialBean.getUsername());
        if(userDetails != null){
            if (!bCryptPasswordEncoder.matches(credentialBean.getPassword(),userDetails.getPassword() )) {
                return "redirect:/index?error";
            }
        }
        return "redirect:/home";
    }



}

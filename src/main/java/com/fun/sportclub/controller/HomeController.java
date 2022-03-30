package com.fun.sportclub.controller;

import com.fun.sportclub.entity.MemberEntity;
import com.fun.sportclub.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    MemberService memberService;

    @GetMapping(value="/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping(value = {"/member/registration"})
    public String showAddContact(Model model) {
        MemberEntity contact = new MemberEntity();
        model.addAttribute("add", true);
        model.addAttribute("contact", contact);

        return "member/member-registration";
    }

    @PostMapping(value = "/member/registration/add")
    public String addContact(Model model, @ModelAttribute("contact") MemberEntity memberRequest) {
        try {
            MemberEntity memberEntity = memberService.saveMember(memberRequest);
            return "redirect:/member/success/" + String.valueOf(memberEntity.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("contact", contact);
            model.addAttribute("add", true);
            return "member-registration";
        }
    }

    @GetMapping(value = {"/member/success/{successId}"})
    public String showAddContact(Model model, @PathVariable("successId") Long successId) {
        MemberEntity memberEntity = memberService.getById(successId).orElse(null);
        model.addAttribute("member", memberEntity);
        return "member/member-success";
    }
}

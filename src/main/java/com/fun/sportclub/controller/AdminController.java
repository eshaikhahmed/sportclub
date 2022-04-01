package com.fun.sportclub.controller;

import com.fun.sportclub.entity.MemberEntity;
import com.fun.sportclub.entity.SportEntity;
import com.fun.sportclub.entity.UserEntity;
import com.fun.sportclub.service.CustomUserService;
import com.fun.sportclub.service.MemberService;
import com.fun.sportclub.service.SportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    MemberService memberService;

    @Autowired
    CustomUserService userService;

    @Autowired
    SportService sportService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = "/admin/member-approval/list")
    public String getContacts(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<MemberEntity> contacts = memberService.findPendingMembers();
        model.addAttribute("contacts", contacts);

        return "admin/member_approval_list";
    }

    @GetMapping(value = {"/admin/manager/list"})
    public String showManagerList(Model model) {
        List<UserEntity> contacts = userService.findAll();
        model.addAttribute("contacts", contacts);

        return "admin/manager_list";
    }

    @GetMapping(value = {"/admin/manager/add"})
    public String addManager(Model model) {
        UserEntity contact = new UserEntity();
        model.addAttribute("add", true);
        model.addAttribute("contact", contact);

        return "admin/add_manager";
    }

    @GetMapping(value = {"/admin/approve/{memberId}"})
    public String approveMember(Model model, @PathVariable("memberId") Long memberId){
        MemberEntity memberEntity = memberService.getById(memberId).orElse(null);
        if(null == memberEntity){
            return "redirect:/admin/member-approval/list?approved=false";
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(memberEntity.getEmail());
        userEntity.setFirstName(memberEntity.getFirstName());
        userEntity.setLastName(memberEntity.getLastName());
        userEntity.setGender(memberEntity.getGender());
        userEntity.setAddress(memberEntity.getAddress());
        userEntity.setUserType(memberEntity.getUserType());
        userEntity.setStatus("APPROVED");
        userEntity.setPassword(passwordEncoder.encode("member@123"));
        userEntity.setBirthDate(memberEntity.getBirthDate());
        userService.saveMember(userEntity);

        return "redirect:/admin/member-approval/list?approved=true";
    }

    @GetMapping(value = {"/admin/sport/list"})
    public String showAddContact(Model model) {
        List<SportEntity> contacts = sportService.findAll();
        model.addAttribute("contacts", contacts);

        return "admin/sport_list";
    }

    @GetMapping(value = {"/admin/sport/add"})
    public String addSportPage(Model model) {
        SportEntity contact = new SportEntity();
        model.addAttribute("add", true);
        model.addAttribute("contact", contact);

        return "admin/add_sport";
    }

    @PostMapping(value = "/admin/sport/add")
    public String saveSport(Model model, @ModelAttribute("contact") SportEntity sportRequest
    , @RequestParam("image1") MultipartFile multipartFile, @RequestParam("image2") MultipartFile multipartFile2) {
        try {

           String status = sportService.saveSportImages(sportRequest, multipartFile, multipartFile2);
           if("Done".equals(status)) {
               sportRequest = sportService.saveSport(sportRequest);
           }

            return "redirect:/admin/sport/list";
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("contact", contact);
            model.addAttribute("add", true);
            return "admin/add_sport";
        }
    }

}

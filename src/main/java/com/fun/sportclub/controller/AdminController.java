package com.fun.sportclub.controller;

import com.fun.sportclub.entity.MemberEntity;
import com.fun.sportclub.entity.SportEntity;
import com.fun.sportclub.entity.SubscriptionEntity;
import com.fun.sportclub.entity.UserEntity;
import com.fun.sportclub.form.AssignManager;
import com.fun.sportclub.service.CustomUserService;
import com.fun.sportclub.service.MemberService;
import com.fun.sportclub.service.SportService;
import com.fun.sportclub.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    SubscriptionService subscriptionService;

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
        List<UserEntity> contacts = userService.findByUserType("MANAGER");
        model.addAttribute("contacts", contacts);

        return "admin/manager_list";
    }

    @GetMapping(value = {"/admin/subscription/list"})
    public String showSubscription(Model model) {
        List<SubscriptionEntity> contacts = subscriptionService.findAll();
        model.addAttribute("contacts", contacts);
        model.addAttribute("assignManager", new AssignManager());
        model.addAttribute("manager_list", userService.findByUserType("MANAGER"));
        return "admin/subscription_list";
    }

    @PostMapping(value = "/admin/assign/manager")
    public String assignManager(Model model, @ModelAttribute("assignManager") AssignManager userRequest) {

        SubscriptionEntity subscriptionEntity = subscriptionService.getById(userRequest.getSubscriptionId()).orElse(null);
        UserEntity userEntity = userService.getById(userRequest.getManagerId()).orElse(null);

        subscriptionEntity.setManager(userEntity);
        subscriptionEntity.setStatus("PENDING_MANAGER");

        subscriptionService.saveSubscription(subscriptionEntity);

        return "redirect:/admin/subscription/list";
    }

    @GetMapping(value = {"/admin/manager/add"})
    public String addManager(Model model) {
        UserEntity contact = new UserEntity();
        model.addAttribute("add", true);
        model.addAttribute("contact", contact);

        return "admin/add_manager";
    }

    @PostMapping(value = "/admin/manager/add")
    public String addContact(Model model, @ModelAttribute("contact") UserEntity userRequest) {
        try {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userRequest.setUserType("MANAGER");
            userRequest.setStatus("APPROVED");
            UserEntity userEntity = userService.saveMember(userRequest);
            return "redirect:/admin/manager/list";
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

    @GetMapping(value = {"/admin/approve/{memberId}"})
    public String approveMember(Model model, @PathVariable("memberId") Long memberId) {
        MemberEntity memberEntity = memberService.getById(memberId).orElse(null);
        if (null == memberEntity) {
            return "redirect:/admin/member-approval/list?approved=false";
        }

        memberEntity.setStatus("APPROVED");
        memberService.saveMember(memberEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(memberEntity.getEmail());
        userEntity.setFirstName(memberEntity.getFirstName());
        userEntity.setLastName(memberEntity.getLastName());
        userEntity.setGender(memberEntity.getGender());
        userEntity.setAddress(memberEntity.getAddress());
        userEntity.setUserType("MEMBER");
        userEntity.setStatus("APPROVED");
        userEntity.setPassword(passwordEncoder.encode(memberEntity.getPassword()));
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

    @GetMapping(value = {"/admin/sport/{sportId}/edit"})
    public String addSportPage(Model model, @PathVariable("sportId") Long sportId) {
        SportEntity contact = sportService.getById(sportId).orElse(null);
        model.addAttribute("add", false);
        model.addAttribute("contact", contact);

        return "admin/edit_sport";
    }

    @PostMapping(value = {"/admin/sport/{sportId}/edit"})
    public String editSaveSport(Model model, @ModelAttribute("contact") SportEntity sportRequest,
                                @PathVariable("sportId") Long sportId, @RequestParam("image1") MultipartFile multipartFile,
                                @RequestParam("image2") MultipartFile multipartFile2) {
        try {
            String status = sportService.saveSportImages(sportRequest, multipartFile, multipartFile2);

            if ("Done".equals(status)) {
                sportRequest = sportService.saveSport(sportRequest);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/sport/list";
    }

    @PostMapping(value = "/admin/sport/add")
    public String saveSport(Model model, @ModelAttribute("contact") SportEntity sportRequest
            , @RequestParam("image1") MultipartFile multipartFile, @RequestParam("image2") MultipartFile multipartFile2) {
        try {

            String status = sportService.saveSportImages(sportRequest, multipartFile, multipartFile2);
            if ("Done".equals(status)) {
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

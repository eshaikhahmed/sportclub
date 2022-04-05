package com.fun.sportclub.controller;

import com.fun.sportclub.entity.*;
import com.fun.sportclub.form.AssignManager;
import com.fun.sportclub.service.BatchMemberService;
import com.fun.sportclub.service.BatchService;
import com.fun.sportclub.service.SportService;
import com.fun.sportclub.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
public class UserController {

    @Autowired
    SportService sportService;

    @Autowired
    SubscriptionService subscriptionService;


    @Autowired
    BatchService batchService;

    @Autowired
    BatchMemberService batchMemberService;

    @GetMapping(value = {"/user/sport/list"})
    public String showAddContact(Model model) {
        List<SportEntity> contacts = sportService.findAll();
        model.addAttribute("contacts", contacts);

        return "user/sport_list";
    }


    @GetMapping(value = {"/user/subscription/list"})
    public String showUserSubscription(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) auth.getPrincipal();
        System.out.println("Current userId "+currentUser.getUserEntity().getId());
        List<SubscriptionEntity> contacts = subscriptionService.findCurrentUserSubscriptions(currentUser.getUserEntity());
        System.out.println(contacts);
        model.addAttribute("contacts", contacts);
        model.addAttribute("assignManager", new AssignManager());

        return "user/subscription_list";
    }

    @GetMapping(value = {"/user/subscription/{subscribeId}"})
    public String showSubscriptionDetail(Model model, @PathVariable("subscribeId") Long subscribeId) {
        SubscriptionEntity subscriptionEntity = subscriptionService.getById(subscribeId).orElse(null);
        model.addAttribute("contact", subscriptionEntity);

        List<BatchMemberEntity> batchMemberEntityList = batchMemberService.findBySubscription(subscriptionEntity);
        if(batchMemberEntityList.size() > 0) {
            BatchEntity batchEntity = batchMemberEntityList.get(0).getBatch();
            model.addAttribute("batch", batchEntity);
            model.addAttribute("members", batchMemberService.findByBatch(batchEntity));
        }
        return "user/subscription_detail";
    }

    @GetMapping(value = {"/user/subscribe/{sportId}"})
    public String subscribeSport(Model model, @PathVariable("sportId") Long sportId) {
        SportEntity sportEntity = sportService.getById(sportId).orElse(null);

        if(sportEntity != null) {
            SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
            subscriptionEntity.setSport(sportEntity);


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails currentUser = (CustomUserDetails) auth.getPrincipal();

            subscriptionEntity.setUser(currentUser.getUserEntity());
            subscriptionService.saveSubscription(subscriptionEntity);
        }

        return "redirect:/user/subscription/list";
    }
}

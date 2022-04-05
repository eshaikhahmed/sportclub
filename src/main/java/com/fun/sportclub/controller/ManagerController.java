package com.fun.sportclub.controller;

import com.fun.sportclub.entity.*;
import com.fun.sportclub.form.AssignBatch;
import com.fun.sportclub.service.BatchMemberService;
import com.fun.sportclub.service.BatchService;
import com.fun.sportclub.service.SportService;
import com.fun.sportclub.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class ManagerController {
    @Autowired
    SportService sportService;

    @Autowired
    BatchService batchService;

    @Autowired
    BatchMemberService batchMemberService;

    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping(value = {"/manager/batch/add"})
    public String showSubscription(Model model) {
        BatchEntity batch = new BatchEntity();

        model.addAttribute("contact", batch);
        model.addAttribute("sportList", sportService.findAll());

        return "manager/add_batch";
    }

    @PostMapping(value = "/manager/batch/add")
    public String addBatch(Model model, @ModelAttribute("contact") BatchEntity userRequest) {

        BatchEntity batchEntity = batchService.saveBatch(userRequest);

        return "redirect:/manager/batch/list";
    }

    @GetMapping(value = {"/manager/batch/list"})
    public String showBatchList(Model model) {
        List<BatchEntity> contacts = batchService.findAll();
        model.addAttribute("contacts", contacts);

        return "manager/batch_list";
    }

    @GetMapping(value = {"/manager/approval/list"})
    public String showApprovalList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) auth.getPrincipal();

        List<SubscriptionEntity> contacts = subscriptionService.findManagerApprovals("PENDING_MANAGER", currentUser.getUserEntity());
        model.addAttribute("contacts", contacts);
        model.addAttribute("assignManager", new AssignBatch());
        model.addAttribute("batch_list", batchService.findAll());

        return "manager/approval_list";
    }

    @PostMapping(value = {"/manager/assign/batch"})
    public String assignBatch(Model model, @ModelAttribute("assignManager") AssignBatch assignBatch){
        BatchEntity batchEntity = batchService.getById(assignBatch.getBatchId()).orElse(null);
        SubscriptionEntity subscriptionEntity = subscriptionService.getById(assignBatch.getSubscriptionId()).orElse(null);

        subscriptionEntity.setStatus("APPROVED");
        subscriptionService.saveSubscription(subscriptionEntity);

        BatchMemberEntity batchMemberEntity = new BatchMemberEntity();
        batchMemberEntity.setBatch(batchEntity);
        batchMemberEntity.setSubscription(subscriptionEntity);
        batchMemberEntity.setUser(subscriptionEntity.getUser());

        batchMemberService.saveBatchMember(batchMemberEntity);
        return "redirect:/manager/approval/list";
    }

    @GetMapping(value = {"/manager/batch/edit/{batchId}"})
    public String editBatch(Model model, @PathVariable("batchId") Long batchId){
        BatchEntity batchEntity = batchService.getById(batchId).orElse(null);
        model.addAttribute("contact", batchEntity);
        model.addAttribute("sportList", sportService.findAll());

        return "manager/edit_batch";
    }

    @PostMapping(value = {"/manager/batch/edit/{batchId}"})
    public String saveEditBatch(Model model, @PathVariable("batchId") Long batchId,
                            @ModelAttribute("contact") BatchEntity userRequest){
        BatchEntity batchEntity = batchService.saveBatch(userRequest);

        return "redirect:/manager/batch/list";
    }
}

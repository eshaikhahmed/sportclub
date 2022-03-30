package com.fun.sportclub.controller;

import com.fun.sportclub.entity.MemberEntity;
import com.fun.sportclub.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    MemberService memberService;

    @GetMapping(value = "/admin/member-approval/list")
    public String getContacts(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<MemberEntity> contacts = memberService.findPendingMembers();
        model.addAttribute("contacts", contacts);

        return "admin/member_approval_list";
    }
}

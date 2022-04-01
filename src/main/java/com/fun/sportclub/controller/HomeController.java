package com.fun.sportclub.controller;

import com.fun.sportclub.entity.MemberEntity;
import com.fun.sportclub.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@Slf4j
public class HomeController {

    @Value("${upload.customer.path}")
    private String customerPath;

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

    @RequestMapping(path = "/member/image/{image}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable("image") String image) throws IOException {
        File file = new File(customerPath + image);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+image);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}

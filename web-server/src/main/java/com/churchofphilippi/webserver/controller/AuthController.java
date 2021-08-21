package com.churchofphilippi.webserver.controller;

import com.churchofphilippi.webserver.model.AllMembers;
import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.service.AllMemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/auth")
public class AuthController {

    private final AllMemberService memberService;

    @GetMapping("/forgot-password/{username}")
    public ResponseEntity<?> getMember(@PathVariable("username") String username) {
        return ResponseEntity.ok(memberService.loadUserByUsername(username));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody AllMembers member) {
        return ResponseEntity.ok(memberService.resetPassword(member));
    }
}

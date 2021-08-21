package com.churchofphilippi.webserver;

import com.churchofphilippi.webserver.model.AllMembers;
import com.churchofphilippi.webserver.service.AllMemberService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class main {

    private static AllMemberService allMemberService;

    public static void func() {
        List<AllMembers> members = allMemberService.getPastors();
        System.out.println("hehe");
    }

    public static void main(String[] args) {

        func();

    }
}

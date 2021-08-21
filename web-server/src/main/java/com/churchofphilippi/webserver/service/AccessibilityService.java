package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.model.Accessibility;
import com.churchofphilippi.webserver.model.AllMembers;
import com.churchofphilippi.webserver.repository.AllMembersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccessibilityService {

    private final AllMembersRepository allMembersRepository;

    public Accessibility getAccessibility(Long memberId) {
        AllMembers financeMember = allMembersRepository.findFinanceDeptMember(memberId, 9000);
        return new Accessibility();
    }
}

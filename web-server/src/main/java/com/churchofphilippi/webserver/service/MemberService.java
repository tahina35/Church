package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.exception.exceptionModel.EmailTakenException;
import com.churchofphilippi.webserver.exception.exceptionModel.MemberNotFoundException;
import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.model.specification.MemberSpecification;
import com.churchofphilippi.webserver.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService, BaseService<Member> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String MEMBER_NOT_FOUND = "Member with email %s not found";
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(String.format(MEMBER_NOT_FOUND, email)));
    }

    public Member registerMember(Member member) {
        boolean memberExists = memberRepository.findByEmail(member.getEmail()).isPresent();
        if(memberExists) {
            throw new EmailTakenException("Email already Taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        memberRepository.save(member);

        return member;
    }

    public Member resetPassword(Member member) {
        String encodedPassword = bCryptPasswordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member save(Member entity) {
        return memberRepository.save(entity);
    }

    @Override
    public void delete(Member entity) {
        memberRepository.delete(entity);
    }

    public Page<Member> findPaginated(int pageNo, int pageSize, String dept, String position) {
        Sort sort = Sort.by("kfname").ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        if(dept.compareTo("-1") != 0 && dept.compareTo("") != 0 && position.compareTo("-1") != 0 && position.compareTo("") != 0) {
            return memberRepository.findByDeptAndPosition(Long.parseLong(dept), Long.parseLong(position), pageable);
        } else if(dept.compareTo("-1") != 0 && dept.compareTo("") != 0) {
            return memberRepository.findByDept(Long.parseLong(dept), pageable);
        } else if(position.compareTo("-1") != 0 && position.compareTo("") != 0) {
            return memberRepository.findByPosition(Long.parseLong(position), pageable);
        } else {
            return memberRepository.findAll(pageable);
        }
    }

    public Page<Member> findAllWithFilters(String searchValue, int pageNo, int pageSize) {
        Sort sort = Sort.by("kfname").ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        MemberSpecification specification = new MemberSpecification(searchValue);
        return memberRepository.findAll(specification, pageable);
    }

    public Member getById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("User not found"));
    }

    public List<Member> getPreachers() {
        return memberRepository.getPreachers();
    }

    public List<Member> getByDepartment(Long deptid) {
        return memberRepository.findByDepartemnt(deptid);
    }

    public Page<Member> findMemberByDeptPaginated(Long deptId, int pageNo, int pageSize) {
        Sort sort = Sort.by("kfname").ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return  memberRepository.findByDept(deptId, pageable);
    }

    public List<Member> getPastors() {
        Member adminPastor = getAdminPastor();
        List<Member> pastors = new ArrayList<>();
        pastors.add(adminPastor);
        pastors.addAll(memberRepository.findPastors());
        return pastors;
    }

    public Member getAdminPastor() {
        return memberRepository.findAdminPastor();
    }
}

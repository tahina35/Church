package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.exception.exceptionModel.EmailTakenException;
import com.churchofphilippi.webserver.exception.exceptionModel.MemberNotFoundException;
import com.churchofphilippi.webserver.model.AllMembers;
import com.churchofphilippi.webserver.model.specification.MemberSpecification;
import com.churchofphilippi.webserver.repository.AllMembersRepository;
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
public class AllMemberService implements UserDetailsService, BaseService<AllMembers> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String MEMBER_NOT_FOUND = "Member with email %s not found";
    private final AllMembersRepository allMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return allMemberRepository.findAllByHomeemail(email).orElseThrow(() -> new MemberNotFoundException(String.format(MEMBER_NOT_FOUND, email)));
    }

    public AllMembers registerMember(AllMembers member) {
        boolean memberExists = allMemberRepository.findAllByHomeemail(member.getHomeemail()).isPresent();
        if(memberExists) {
            throw new EmailTakenException("Email already Taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        allMemberRepository.save(member);

        return member;
    }

    public AllMembers resetPassword(AllMembers member) {
        String encodedPassword = bCryptPasswordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
        return allMemberRepository.save(member);
    }

    @Override
    public List<AllMembers> getAll() {
        return allMemberRepository.findAll();
    }

    @Override
    public AllMembers save(AllMembers entity) {
        return allMemberRepository.save(entity);
    }

    @Override
    public void delete(AllMembers entity) {
        allMemberRepository.delete(entity);
    }

    public Page<AllMembers> findPaginated(int pageNo, int pageSize, String dept, String position) {
        Sort sort = Sort.by("kfname").ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        if(dept.compareTo("-1") != 0 && dept.compareTo("") != 0 && position.compareTo("-1") != 0 && position.compareTo("") != 0) {
            return allMemberRepository.findByDeptAndPosition(Long.parseLong(dept), Long.parseLong(position), pageable);
        } else if(dept.compareTo("-1") != 0 && dept.compareTo("") != 0) {
            return allMemberRepository.findByDept(Long.parseLong(dept), pageable);
        } else if(position.compareTo("-1") != 0 && position.compareTo("") != 0) {
            return allMemberRepository.findByPosition(Long.parseLong(position), pageable);
        } else {
            return allMemberRepository.findAll(pageable);
        }
    }

    public Page<AllMembers> findAllWithFilters(String searchValue, int pageNo, int pageSize) {
        Sort sort = Sort.by("klname").ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        MemberSpecification specification = new MemberSpecification(searchValue);
        return allMemberRepository.findAll(specification, pageable);
    }

    public AllMembers getById(Long id) {
        return allMemberRepository.findByMemberId(id).orElseThrow(() -> new MemberNotFoundException("User not found"));
    }

    public List<AllMembers> getPreachers() {
        return allMemberRepository.getPreachers();
    }

    public List<AllMembers> getByDepartment(Long deptid) {
        return allMemberRepository.findByDepartemnt(deptid);
    }

    public Page<AllMembers> findMemberByDeptPaginated(Long deptId, int pageNo, int pageSize) {
        Sort sort = Sort.by("kfname").ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return  allMemberRepository.findByDept(deptId, pageable);
    }

    public List<AllMembers> getPastors() {
        AllMembers adminPastor = getAdminPastor();
        List<AllMembers> pastors = new ArrayList<>();
        pastors.add(adminPastor);
        pastors.addAll(allMemberRepository.findPastors());
        return pastors;
    }

    public AllMembers getAdminPastor() {
        return allMemberRepository.findAdminPastor();
    }



    public void editSchema() {
        List<AllMembers> members = allMemberRepository.findAll();
        int count = 1;
        for(AllMembers m : members) {
            //korean name
            String kname = m.getFirstname();
            String ename = m.getLastname();
            String klname = "";
            String kfname = "";
            String elname = "";
            String efname = "";
            if(!isAlpha(kname)) {
                klname = new String(new char[]{kname.charAt(0)});
                StringBuilder sb = new StringBuilder();
                for (int i=1; i<kname.length(); i++) {
                    sb.append(kname.charAt(i));
                }
                kfname = sb.toString();
                String[] enameSplitted = ename.split(" ");
                efname = enameSplitted[0];
                for (int i=1; i<enameSplitted.length; i++) {
                    elname += enameSplitted[i] + " ";
                }
            } else {
                efname = kname;
                elname = ename;
            }

            m.setKfname(kfname);
            m.setKlname(klname);
            m.setEfname(efname);
            m.setElname(elname);

            allMemberRepository.save(m);

//            System.out.println(count + "- kname: " + klname + " " + kfname + " | " + "ename: " + efname + " " + elname);
//            count++;
        }

    }

    public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z0-9 ]*$");
    }
}

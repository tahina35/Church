package com.churchofphilippi.webserver.controller;

import com.churchofphilippi.webserver.config.PageConfig;
import com.churchofphilippi.webserver.model.DeptMember;
import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.model.Role;
import com.churchofphilippi.webserver.model.customModels.MemberDetails;
import com.churchofphilippi.webserver.model.customModels.MemberFilters;
import com.churchofphilippi.webserver.model.pagination.CustomPage;
import com.churchofphilippi.webserver.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/member")
public class MemberController {

    private final MemberService memberService;
    private final DeptService deptService;
    private final PositionService positionService;
    private final DeptMemberService deptMemberService;
    private final RoleService roleService;
    private final PageConfig pageConfig;

    @GetMapping
    public ResponseEntity<?> getMembers() {
        return ResponseEntity.ok(memberService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }
    @GetMapping("/department/{id}")
    public ResponseEntity<?> getMemberByDept(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.getByDepartment(id));
    }

    @GetMapping("/department/{id}/page/{pageNo}")
    public ResponseEntity<?> getMemberByDeptPaginated(@PathVariable("id") Long deptId, @PathVariable("pageNo") int pageNo) {
        Page<Member> paginated = memberService.findMemberByDeptPaginated(deptId, pageNo, pageConfig.getMobileSize());
        CustomPage<Member> page = new CustomPage<Member>(pageNo, paginated.getTotalPages(), paginated.getTotalElements(), paginated.getContent());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getMemberById(@PathVariable("username") String username) {
        return ResponseEntity.ok(memberService.loadUserByUsername(username));
    }

    @GetMapping("/{id}/departments")
    public ResponseEntity<?> getDeptsByMemberId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deptMemberService.findByMemberId(id));
    }

    @GetMapping("/{id}/positions")
    public ResponseEntity<?> getPositionsByMemberId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.findByMemberId(id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getMemberDetails(@PathVariable("id") Long id) {
        MemberDetails memberDetails = new MemberDetails();
        memberDetails.setMember(memberService.getById(id));
        memberDetails.setAllDepts(deptService.findDeptsMemberNotBelongTo(id));
        memberDetails.setAllPositions(positionService.findPositionsMemberNotAssignedTo(id));
        memberDetails.setMemberDepts(deptMemberService.findByMemberId(id));
        memberDetails.setMemberPositions(roleService.findByMemberId(id));
        return ResponseEntity.ok(memberDetails);
    }

    @GetMapping("/filters")
    public ResponseEntity<?> getMemberFilters() {
        MemberFilters memberFilters = new MemberFilters();
        memberFilters.setDepts(deptService.getAll());
        memberFilters.setPositions(positionService.getAll());
        return ResponseEntity.ok(memberFilters);
    }

    @PutMapping
    public ResponseEntity<?> save(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.save(member));
    }

    @GetMapping("/page/{pageNo}")
    public ResponseEntity<?> getMembersPaginated(@PathVariable("pageNo")int pageNo, @RequestParam(name = "dept") String dept, @RequestParam(name = "pos") String position) {
        Page<Member> paginated = memberService.findPaginated(pageNo, pageConfig.getSize(), dept, position);
        CustomPage<Member> page = new CustomPage<Member>(pageNo, paginated.getTotalPages(), paginated.getTotalElements(), paginated.getContent());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMember(@RequestParam(name = "v") String value, @RequestParam(name = "page") int pageNo) {
        Page<Member> paginated = memberService.findAllWithFilters(value, pageNo, pageConfig.getSize());
        CustomPage<Member> page = new CustomPage<Member>(pageNo, paginated.getTotalPages(), paginated.getTotalElements(), paginated.getContent());
        return ResponseEntity.ok(page);
    }

    @PostMapping("/add-to-department")
    public ResponseEntity<?> addToDepartment(@RequestBody DeptMember dept) {
        return ResponseEntity.ok(deptMemberService.save(dept));
    }

    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRole(@RequestBody Role position) {
        return ResponseEntity.ok(roleService.save(position));
    }

    @PostMapping("/remove-from-department")
    public ResponseEntity<?> removeFromDepartment(@RequestBody DeptMember deptMember) {
        deptMemberService.delete(deptMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/remove-position")
    public ResponseEntity<?> removePosition(@RequestBody Role role) {
        roleService.delete(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pastors")
    public ResponseEntity<?> getPastors() {
        return ResponseEntity.ok(memberService.getPastors());
    }
}

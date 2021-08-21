package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.AllMembers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface AllMembersRepository extends JpaRepository<AllMembers, Long>, JpaSpecificationExecutor<AllMembers> {

    Optional<AllMembers> findAllByHomeemail(String email);

    Optional<AllMembers> findByMemberId(Long id);

    @Query(
            value = "SELECT m.* FROM all_members m INNER JOIN dept_member dm ON m.member_id = dm.member_id INNER JOIN role r on m.member_id = r.member_id WHERE dm.dept_id = ?1 AND r.position_id = ?2",
            nativeQuery = true
    )
    Page<AllMembers> findByDeptAndPosition(Long dept, Long position, Pageable pageable);

    @Query(
            value = "select m.* from all_members m join dept_member dm on m.member_id = dm.member_id where dm.dept_id = ?1",
            nativeQuery = true
    )
    Page<AllMembers> findByDept(Long dept, Pageable pageable);

    @Query(
            value = "select m.* from all_members m inner join role r on m.member_id = r.member_id where r.position_id = ?1",
            nativeQuery = true
    )
    Page<AllMembers> findByPosition(Long position, Pageable pageable);

    @Query(
            value = "SELECT m.* FROM all_members m INNER JOIN role r ON m.member_id = r.member_id GROUP BY m.member_id",
            nativeQuery = true
    )
    List<AllMembers> getPreachers();

    @Query(
            value = "SELECT m.* FROM all_members m INNER JOIN dept_member dm ON m.member_id = dm.member_id WHERE dm.dept_id = ?1",
            nativeQuery = true
    )
    List<AllMembers> findByDepartemnt(Long deptId);

    @Query(
            value = "SELECT m.* FROM all_members m INNER JOIN dept_member dm ON m.member_id = dm.member_id WHERE dm.dept_id = ?1",
            nativeQuery = true
    )
    Page<AllMembers> findByDepartemntPaginated(long deptId, Pageable pageable);

    @Query(
            value = "SELECT m.* FROM all_members m INNER JOIN role r ON m.member_id = r.member_id INNER JOIN position p ON p.position_id = r.position_id WHERE p.position_id = 2",
            nativeQuery = true
    )
    AllMembers findAdminPastor();

    @Query(
            value = "SELECT m.* FROM all_members m INNER JOIN role r ON m.member_id = r.member_id INNER JOIN position p ON p.position_id = r.position_id WHERE p.position_id = 1",
            nativeQuery = true
    )
    List<AllMembers> findPastors();

    @Query(value = "SELECT m.* FROM all_members m INNER JOIN dept_member dm ON m.member_id = dm.member_id INNER JOIN dept d ON d.dept_id = dm.dept_id WHERE m.member_id = ?1 AND d.dept_code = ?2")
    AllMembers findFinanceDeptMember(long memberId, int deptCode);
}

package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    Optional<Member> findByEmail(String email);

    @Override
    Optional<Member> findById(Long aLong);

    @Query(
            value = "SELECT m.* FROM member m INNER JOIN dept_member dm ON m.member_id = dm.member_id INNER JOIN role r on m.member_id = r.member_id WHERE dm.dept_id = ?1 AND r.position_id = ?2",
            nativeQuery = true
    )
    Page<Member> findByDeptAndPosition(Long dept, Long position, Pageable pageable);

    @Query(
            value = "select m.* from member m join dept_member dm on m.member_id = dm.member_id where dm.dept_id = ?1",
            nativeQuery = true
    )
    Page<Member> findByDept(Long dept, Pageable pageable);

    @Query(
            value = "select m.* from member m inner join role r on m.member_id = r.member_id where r.position_id = ?1",
            nativeQuery = true
    )
    Page<Member> findByPosition(Long position, Pageable pageable);

    @Query(
            value = "SELECT m.* FROM member m INNER JOIN role r ON m.member_id = r.member_id GROUP BY m.member_id",
            nativeQuery = true
    )
    List<Member> getPreachers();

    @Query(
            value = "SELECT m.* FROM member m INNER JOIN dept_member dm ON m.member_id = dm.member_id WHERE dm.dept_id = ?1",
            nativeQuery = true
    )
    List<Member> findByDepartemnt(Long deptId);

    @Query(
            value = "SELECT m.* FROM member m INNER JOIN dept_member dm ON m.member_id = dm.member_id WHERE dm.dept_id = ?1",
            nativeQuery = true
    )
    Page<Member> findByDepartemntPaginated(long deptId, Pageable pageable);

    @Query(
            value = "SELECT m.* FROM member m INNER JOIN role r ON m.member_id = r.member_id INNER JOIN position p ON p.position_id = r.position_id WHERE p.position_id = 2",
            nativeQuery = true
    )
    Member findAdminPastor();

    @Query(
            value = "SELECT m.* FROM member m INNER JOIN role r ON m.member_id = r.member_id INNER JOIN position p ON p.position_id = r.position_id WHERE p.position_id = 1",
            nativeQuery = true
    )
    List<Member> findPastors();
}

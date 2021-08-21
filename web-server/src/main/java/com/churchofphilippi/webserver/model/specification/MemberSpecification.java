package com.churchofphilippi.webserver.model.specification;

import com.churchofphilippi.webserver.model.AllMembers;
import com.churchofphilippi.webserver.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MemberSpecification implements Specification<AllMembers> {

    private final String searchValue;

    @Override
    public Predicate toPredicate(Root<AllMembers> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.like(builder.lower(root.get("efname")), "%" + getSearchValue().toLowerCase() + "%"));
        predicates.add(builder.like(builder.lower(root.get("elname")), "%" + getSearchValue().toLowerCase() + "%"));
        predicates.add(builder.like(builder.lower(root.get("kfname")), "%" + getSearchValue().toLowerCase() + "%"));
        predicates.add(builder.like(builder.lower(root.get("klname")), "%" + getSearchValue().toLowerCase() + "%"));
        predicates.add(builder.like(builder.lower(root.get("homeemail")), "%" + getSearchValue().toLowerCase() + "%"));

        return builder.or(predicates.toArray(new Predicate[0]));
    }
}

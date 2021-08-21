package com.churchofphilippi.webserver.model.customModels;

import com.churchofphilippi.webserver.model.AllMembers;
import com.churchofphilippi.webserver.model.Dept;
import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.model.pagination.CustomPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentData {
    private List<Dept> depts;
    private CustomPage<AllMembers> members;
}

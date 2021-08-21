package com.churchofphilippi.webserver.model.customModels;

import com.churchofphilippi.webserver.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetails {

    private AllMembers member;
    private List<Dept> allDepts;
    private List<Position> allPositions;
    private List<DeptMember> memberDepts;
    private List<Role> memberPositions;

}

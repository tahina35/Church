package com.churchofphilippi.webserver.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequest {

    private final String fname;
    private final String lname;
    private final String email;
    private final String gender;
    private final String streetName;
    private final String streetAddress;
    private final String aptNumber;
    private final String city;
    private final String state;
    private final Integer zipCode;
    private final String phoneNumber;

}

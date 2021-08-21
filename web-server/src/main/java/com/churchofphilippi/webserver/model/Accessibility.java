package com.churchofphilippi.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accessibility {

    private boolean newForm = true;
    private boolean signer;
    private boolean payment;
    private boolean transaction = true;
    private boolean bugdet;

}

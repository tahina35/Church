package com.churchofphilippi.webserver.model.keys;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class SignatureKey implements Serializable {

    Long memberId;
    Long paymentRequestId;
}

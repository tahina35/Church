package com.churchofphilippi.webserver.model.keys;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PrExpenseItemKey implements Serializable {

    Long expenseItemID;
    Long paymentRequestId;
}

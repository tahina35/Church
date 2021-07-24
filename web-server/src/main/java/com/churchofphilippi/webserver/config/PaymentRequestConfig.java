package com.churchofphilippi.webserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.payment-request.status")
@Getter
@Setter
public class PaymentRequestConfig {

    private String needSignature;
    private String needPayment;
    private String completed;
    private String returned;

}

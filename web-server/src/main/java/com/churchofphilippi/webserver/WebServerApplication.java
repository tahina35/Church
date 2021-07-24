package com.churchofphilippi.webserver;

import com.churchofphilippi.webserver.config.GeneralConfig;
import com.churchofphilippi.webserver.config.MobileConfig;
import com.churchofphilippi.webserver.config.PageConfig;
import com.churchofphilippi.webserver.config.PaymentRequestConfig;
import com.churchofphilippi.webserver.jwt.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class, PageConfig.class, MobileConfig.class, PaymentRequestConfig.class, GeneralConfig.class})
public class WebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://cop-web-server.s3-website.us-east-2.amazonaws.com", "http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Acces-Control-Allow-Origin", "Content-Type", "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
                "Acces-Control-Request-Method", "Acces-Control-Request-Headers"));
        config.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "Acces-Control-Allow-Origin", "Acces-Control-Allow-Credentials"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}

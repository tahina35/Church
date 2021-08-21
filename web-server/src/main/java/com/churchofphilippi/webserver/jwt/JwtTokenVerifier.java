package com.churchofphilippi.webserver.jwt;

import com.churchofphilippi.webserver.exception.exceptionModel.TokenNotTrustedException;
import com.churchofphilippi.webserver.service.AllMemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AllMemberService memberService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                    new HashSet<GrantedAuthority>()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, new TokenNotTrustedException("Token invalid or expired"));
            return;
        }

        filterChain.doFilter(request, response);
    }
}

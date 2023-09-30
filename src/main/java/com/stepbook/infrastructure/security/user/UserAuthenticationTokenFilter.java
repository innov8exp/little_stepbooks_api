package com.stepbook.infrastructure.security.user;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserAuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService endUserDetailsService;
    private final UserJwtTokenProvider jwtTokenProvider;
    @Value("${jwt.header}")
    private String authHeader;
    @Value("${jwt.token-type}")
    private String tokenType;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().contains("/auth")
                || request.getServletPath().contains("/swagger-ui")
                || request.getServletPath().contains("/api-docs")
                || request.getServletPath().contains("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }
//        if (request.getRequestURI().startsWith("/admin")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        if (request.getRequestURI().startsWith("/auth/login")
//                || request.getRequestURI().startsWith("/auth/social-login")
//                || request.getRequestURI().startsWith("/auth/guest-login")
//                || request.getRequestURI().startsWith("/auth/register")
//                || request.getRequestURI().startsWith("/auth/reset-password")
//                || request.getRequestURI().startsWith("/auth/validation")
//                || request.getRequestURI().startsWith("/auth/verification")
//                || request.getRequestURI().startsWith("/auth/logout")
//                || request.getRequestURI().startsWith("/auth/refresh-token")
//                || request.getRequestURI().startsWith("/v3/api-docs")
//                || request.getRequestURI().startsWith("/swagger-ui")
//                || request.getRequestURI().startsWith("/actuator")
//        ) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        String accessToken = request.getHeader(authHeader);
        if (ObjectUtils.isEmpty(accessToken)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        String username = jwtTokenProvider.getSubjectFromToken(accessToken);
        if (ObjectUtils.isEmpty(username)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = endUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                filterChain.doFilter(request, response);
                return;
            }
            if (jwtTokenProvider.verifyToken(accessToken, userDetails)) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

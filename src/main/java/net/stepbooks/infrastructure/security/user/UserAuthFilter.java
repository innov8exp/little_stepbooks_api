package net.stepbooks.infrastructure.security.user;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService endUserDetailsService;
    private final UserJwtTokenProvider jwtTokenProvider;
    @Value("${jwt.header}")
    private String authHeader;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //log.debug("user doFilterInternal: {}", request.getServletPath());
        String accessToken = request.getHeader(authHeader);
        if (ObjectUtils.isEmpty(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = jwtTokenProvider.getSubjectFromToken(accessToken);
        if (ObjectUtils.isEmpty(username)) {
            filterChain.doFilter(request, response);
            return;
        }
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
        filterChain.doFilter(request, response);
    }
}

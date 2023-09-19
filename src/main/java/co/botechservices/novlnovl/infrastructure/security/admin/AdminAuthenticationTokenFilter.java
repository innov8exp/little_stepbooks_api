package co.botechservices.novlnovl.infrastructure.security.admin;

import org.springframework.beans.factory.annotation.Qualifier;
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AdminAuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailServices;
    private final AdminJwtTokenProvider adminJwtTokenProvider;
    @Value("${admin.jwt.access-token.cookie-name}")
    private String accessTokenCookieName;
    @Value("${admin.jwt.header}")
    private String authHeader;
    @Value("${admin.jwt.token-type}")
    private String tokenType;

    public AdminAuthenticationTokenFilter(@Qualifier("adminDetailsServiceImpl") UserDetailsService userDetailServices,
                                          AdminJwtTokenProvider adminJwtTokenProvider) {
        this.userDetailServices = userDetailServices;
        this.adminJwtTokenProvider = adminJwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getRequestURI().startsWith("/admin")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().startsWith("/admin/auth/login")
                || request.getRequestURI().startsWith("/admin/auth/register")
                || request.getRequestURI().startsWith("/admin/auth/logout")
                || request.getRequestURI().startsWith("/admin/auth/refresh-token")
                || request.getRequestURI().startsWith("/v3/api-docs")
                || request.getRequestURI().startsWith("/swagger-ui")
                || request.getRequestURI().startsWith("/actuator")
        ) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (ObjectUtils.isEmpty(cookies)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        for (Cookie cookie : cookies) {
            if (accessTokenCookieName.equals(cookie.getName())) {
                accessToken = cookie.getValue();
                break;
            }
        }
        if (ObjectUtils.isEmpty(accessToken)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        String username = adminJwtTokenProvider.getSubjectFromToken(accessToken);
        if (ObjectUtils.isEmpty(username)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        if ("/admin/auth/refresh-token".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        UserDetails userDetails;
        try {
            userDetails = userDetailServices.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            filterChain.doFilter(request, response);
            return;
        }
        if (adminJwtTokenProvider.verifyToken(accessToken, userDetails)) {
            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        filterChain.doFilter(request, response);
    }
}

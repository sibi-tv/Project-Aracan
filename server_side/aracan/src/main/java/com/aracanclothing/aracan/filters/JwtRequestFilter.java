package com.aracanclothing.aracan.filters;

import com.aracanclothing.aracan.services.jwt.UserDetailsServiceImpl;
import com.aracanclothing.aracan.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth_header = request.getHeader("Authorization");
        String token = null;
        String user_name = null;

        if (auth_header != null && auth_header.startsWith("Bearer ")) {
            token = auth_header.substring(7);
            user_name = jwtUtil.retrieveUsername(token);
        }

        if (user_name != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user_name);


            if (jwtUtil.validToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken auth_token = new UsernamePasswordAuthenticationToken(userDetails, null);
                auth_token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth_token);
            }
        }

        filterChain.doFilter(request, response);
    }
}

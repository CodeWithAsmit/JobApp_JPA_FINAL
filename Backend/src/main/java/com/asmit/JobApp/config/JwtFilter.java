package com.asmit.JobApp.config;

import com.asmit.JobApp.service.JwtService;
import com.asmit.JobApp.service.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter
{
    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String token = null;
        String userName = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if ("token".equals(cookie.getName()))
                {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            try
            {
                userName = jwtService.extractUserName(token);

                if (userName != null)
                {
                    UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);

                    if (jwtService.validateToken(token, userDetails))
                    {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                    else
                    {
                        System.out.println("JWT Token validation failed (expired or username mismatch).");
                        expireAuthCookie(response);
                        response.sendRedirect("/login");
                        return;
                    }
                }
                else
                {
                    System.out.println("Could not extract username from JWT token.");
                    expireAuthCookie(response);
                    response.sendRedirect("/login");
                    return;
                }
            }
            catch (SignatureException e)
            {
                System.out.println("JWT Signature validation failed: " + e.getMessage());
                expireAuthCookie(response);
                response.sendRedirect("/login"); 
                return;
            }
            catch (ExpiredJwtException e)
            {
                System.out.println("JWT Token has expired: " + e.getMessage());
                expireAuthCookie(response);
                response.sendRedirect("/login");
                return;
            }
            catch (MalformedJwtException e)
            {
                System.out.println("JWT Token is malformed: " + e.getMessage());
                expireAuthCookie(response);
                response.sendRedirect("/login");
                return;
            }
            catch (Exception e)
            {
                System.out.println("An unexpected error occurred during JWT processing: " + e.getMessage());
                expireAuthCookie(response);
                response.sendRedirect("/login");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void expireAuthCookie(HttpServletResponse response)
    {
        ResponseCookie expiredCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
    }
}
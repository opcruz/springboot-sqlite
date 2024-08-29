package com.demo.sqlite.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

   private final static String HEADER_AUTHORIZATION_KEY = "Authorization";
   private final static String TOKEN_BEARER_PREFIX = "Bearer ";

   @Override
   protected void doFilterInternal(HttpServletRequest request,
         HttpServletResponse response,
         FilterChain filterChain) throws ServletException, IOException {
      try {
         if (isJWTValid(request, response)) {
            Claims claims = parseJWT(request);
            if (claims.get("roles") != null) {
               setUpSpringAuthentication(claims);
            } else {
               SecurityContextHolder.clearContext();
            }
         } else {
            SecurityContextHolder.clearContext();
         }
         filterChain.doFilter(request, response);
      } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException ex) {
         log.warn("Error logging in: {}", ex.getMessage());
         response.setStatus(HttpServletResponse.SC_FORBIDDEN);
         response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
      }
   }

   private Claims parseJWT(HttpServletRequest request) {
      String jwtToken = request.getHeader(HEADER_AUTHORIZATION_KEY).replace(TOKEN_BEARER_PREFIX, "");
      return JWTCoder.parseJWT(jwtToken);
   }

   private void setUpSpringAuthentication(Claims claims) {
      @SuppressWarnings("unchecked")
      List<String> roles = (List<String>) claims.get("roles");
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
            roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

      UserAuthenticateInfo userAuthenticateInfo = UserAuthenticateInfo.builder()
            .userId(claims.get("userId", Integer.class))
            .subject(claims.getSubject())
            .roles(roles)
            .build();
      auth.setDetails(userAuthenticateInfo);
      SecurityContextHolder.getContext().setAuthentication(auth);
   }

   private boolean isJWTValid(HttpServletRequest request, HttpServletResponse res) {
      String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION_KEY);
      return authenticationHeader != null && authenticationHeader.startsWith(TOKEN_BEARER_PREFIX);
   }

}
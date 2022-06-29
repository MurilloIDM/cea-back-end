package com.cea.security.administrator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cea.dto.auth.AccessTokenAdminDTO;
import com.cea.models.Administrator;
import com.cea.utils.ConfigProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticateFilterAdministrator extends UsernamePasswordAuthenticationFilter {

    public static final Integer TOKEN_EXPIRES = 7200000;

    private ConfigProperties configProperties;
    private AuthenticationManager authenticationManager;

    public JWTAuthenticateFilterAdministrator (
            AuthenticationManager authenticationManager,
            ConfigProperties configProperties) {
        this.configProperties = configProperties;
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl("/admin/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            Administrator administrator = new ObjectMapper().readValue(request.getInputStream(), Administrator.class);

            UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(
                    administrator.getUsername(),
                    administrator.getPassword(),
                    new ArrayList<>());

            return authenticationManager.authenticate(userAuth);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar administrador!");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        Administrator administrator = (Administrator) authResult.getPrincipal();

        String TOKEN_PASS = configProperties.getProperty("token.pass.administrator");

        String token = JWT.create()
                .withSubject(administrator.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRES))
                .sign(Algorithm.HMAC512(TOKEN_PASS));

        Gson gson = new Gson();
        AccessTokenAdminDTO accessToken = new AccessTokenAdminDTO(
                administrator.getId(), token, administrator.getIsPrimaryAccess());

        String accessTokenJson = gson.toJson(accessToken);

        response.getWriter().write(accessTokenJson);
        response.getWriter().flush();
    }

}


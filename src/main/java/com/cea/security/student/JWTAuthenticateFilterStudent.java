package com.cea.security.student;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cea.dto.auth.AccessTokenDTO;
import com.cea.models.Student;
import com.cea.utils.ConfigProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
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

public class JWTAuthenticateFilterStudent extends UsernamePasswordAuthenticationFilter {

    public static final Integer TOKEN_EXPIRES = 10800000;

    private final AuthenticationManager authenticationManager;
    private final ConfigProperties configProperties;

    public JWTAuthenticateFilterStudent(AuthenticationManager authenticationManager,
                                        ConfigProperties configProperties) {
        this.configProperties = configProperties;
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl("/app/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                               HttpServletResponse response) throws AuthenticationException {
        try {
            Student student = new ObjectMapper().readValue(request.getInputStream(), Student.class);

            UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(
                    student.getEmail(),
                    student.getPassword(),
                    new ArrayList<>());

            return authenticationManager.authenticate(userAuth);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar estudante!");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        Student student = (Student) authResult.getPrincipal();

        String TOKEN_PASS = configProperties.getProperty("token.pass.student");

        String token = JWT.create()
                .withSubject(student.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRES))
                .sign(Algorithm.HMAC512(TOKEN_PASS));

        Gson gson = new Gson();
        AccessTokenDTO accessToken = new AccessTokenDTO(token);

        String accessTokenJson = gson.toJson(accessToken);

        response.getWriter().write(accessTokenJson);
        response.getWriter().flush();
    }

}

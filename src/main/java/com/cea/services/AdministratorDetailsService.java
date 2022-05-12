package com.cea.services;

import com.cea.repository.AdministratorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdministratorDetailsService implements UserDetailsService {

    private final AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(administratorRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Administrador não encontrado!"));
    }

}

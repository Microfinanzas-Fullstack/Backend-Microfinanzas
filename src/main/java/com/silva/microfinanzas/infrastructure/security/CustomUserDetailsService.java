package com.silva.microfinanzas.infrastructure.security;

import com.silva.microfinanzas.domain.entities.User;
import com.silva.microfinanzas.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementación de UserDetailsService para Spring Security.
 *
 * Carga los detalles del usuario desde la base de datos
 * para la autenticación.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(getAuthorities(user))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.getEnabled())
            .build();
    }

    /**
     * Convierte los roles del usuario en GrantedAuthorities.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}


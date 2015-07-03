package com.wordrails.business;

import java.util.Collection;
import java.util.HashSet;

import com.wordrails.auth.NetworkUsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.PersonRepository;

@Component
public class AccessControllerUtil {
    private @Autowired PersonRepository personRepository;
    private @Autowired @Qualifier("myAuthenticationManager") AuthenticationManager authenticationManager;

    public Person getLoggedPerson() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        User user = new User();

        if (authentication == null
                || (authentication != null && authentication.getPrincipal() instanceof String)
                && authentication.getPrincipal().equals("anonymousUser")) {

            user.username = "wordrails";
            user.password = "wordrails";

            Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            authentication = new NetworkUsernamePasswordAuthenticationToken(
                    user.username, user.password, 1, authorities);

            authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        } else {
            user = (User) authentication.getPrincipal();
        }
        return personRepository.findByUsernameAndNetworkId(user.username,
                user.networkId);
    }

    public boolean isLogged() {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return (personRepository.findByUsernameAndNetworkId(u.username, u.networkId) != null ? true
                : false);
    }

    public void authenticate(String username, String password, Integer networkId) {
        NetworkUsernamePasswordAuthenticationToken auth = new NetworkUsernamePasswordAuthenticationToken(
                username, password, networkId);

        Authentication validAuth = authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(validAuth);
    }

    public boolean areYouLogged(Integer personId) {
        boolean areYouLogged = false;
        Person person = getLoggedPerson();
        if (person != null && person.id == personId) {
            areYouLogged = true;
        }
        return areYouLogged;
    }
}
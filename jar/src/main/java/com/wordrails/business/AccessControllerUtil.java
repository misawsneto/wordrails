package com.wordrails.business;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.wordrails.auth.NetworkUsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.PersonRepository;
import com.wordrails.services.CacheService;

@Component
public class AccessControllerUtil {
    private
    @Autowired
    PersonRepository personRepository;
/*    private
    @Autowired
    UserDetailsService userDetailsService;*/
    private
    @Autowired
    @Qualifier("myAuthenticationManager")
    AuthenticationManager authenticationManager;
    private
    @Autowired
    CacheService cacheService;

    public Person getLoggedPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user;
        if (authentication == null ||
                (authentication != null && authentication.getPrincipal() instanceof String)
                        && authentication.getPrincipal().equals("anonymousUser")) {

            user = new org.springframework.security.core.userdetails.User("wordrails", "wordrails", true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        }
        try {
            return cacheService.getPersonByUsername(user.getUsername());
        } catch (ExecutionException e) {
            return personRepository.findByUsername(user.getUsername());
        }
    }

    public boolean isLogged() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return (cacheService.getPersonByUsername(user.getUsername()) != null ? true : false);
        } catch (ExecutionException e) {
            return (personRepository.findByUsername(user.getUsername()) != null ? true : false);
        }
    }

    public void authenticate(String username, String password, Integer network_id) {
        NetworkUsernamePasswordAuthenticationToken auth = new NetworkUsernamePasswordAuthenticationToken(
                username, password, network_id);

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
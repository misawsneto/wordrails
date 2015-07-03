package com.wordrails.auth;

import com.wordrails.business.User;
import com.wordrails.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by jonas on 29/06/15.
 */
@Component
public class AuthenticationNetwork implements AuthenticationProvider {

    private @Autowired
    UserRepository ur;

    @Autowired
    public AuthenticationNetwork(UserRepository ur) {
        if(ur == null){
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.ur = ur;
    }

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        // TODO Auto-generated method stub
        NetworkUsernamePasswordAuthenticationToken token =
                (NetworkUsernamePasswordAuthenticationToken) auth;

        String username = token.getName();
        String password = (String) token.getCredentials();
        Integer networkId = Integer.valueOf(token.getNetwork());

//		final Person p = personRepo.findByUsernameAndNetworkId(username, networkId);
        final User u;
        u = ur.findByUsernameAndPasswordAndNetworkId(username, password, networkId);

        //These exceptions are not being thrown, not in a visible way at least
        if (u == null) {
            throw new BadCredentialsException("Username not found");
        }

        if (!password.equals(u.password)) {
            throw new BadCredentialsException("Wrong password");
        }

        if(networkId == null){
            throw new BadCredentialsException("Wrong network_id");
        }

        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new NetworkUsernamePasswordAuthenticationToken(u, password,
                networkId, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (NetworkUsernamePasswordAuthenticationToken
                .class
                .equals(authentication));
    }
}

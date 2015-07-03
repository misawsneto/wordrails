package com.wordrails.auth;

import com.wordrails.business.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by jonas on 29/06/15.
 */
public class NetworkUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Integer networkId;

    public NetworkUsernamePasswordAuthenticationToken(String username, String password){
        super(username, password);
        this.networkId = 1;
    }

    public NetworkUsernamePasswordAuthenticationToken(String principal,
                                                      String credentials, Integer networkId) {
        super(principal, credentials);
        this.networkId = networkId;
    }

    public NetworkUsernamePasswordAuthenticationToken(String username,
                                                      String credentials, Integer networkId,
                                                      Collection<GrantedAuthority> authorities){
        super(username, credentials, authorities);
        this.networkId = networkId;
    }

    public NetworkUsernamePasswordAuthenticationToken(User u,
                                                      String credentials, Integer networkId,
                                                      Collection<GrantedAuthority> authorities){
        super(u, credentials, authorities);
        this.networkId = networkId;
    }

    public Integer getNetwork(){
        return this.networkId;
    }

    private static final long serialVersionUID = -5138870746127783L;
}

package com.wordrails.filter;

import com.wordrails.WordrailsService;
import com.wordrails.business.Network;
import com.wordrails.business.User;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class TrixAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CacheService cacheService;

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = super.obtainUsername(request);
		String password = super.obtainPassword(request);
		Network network = wordrailsService.getNetworkFromHost(request);
		Set<User> users;
		try {
			users = cacheService.getUsersByUsername(username);
		} catch (ExecutionException e) {
			users = userRepository.findByUsernameAndEnabled(username, true);
		}

		if (users != null && users.size() > 0) {
			User user = null;
			for (User u : users) {
				if (Objects.equals(u.networkId, network.id)) { //find by network
					if (password.equals(u.password)) { //if this is the user for this network, is the password right?
						user = u;
						break;
					}
				} else if (password.equals(u.password)) { //find by password, if it enters here, the network is not set
					user = u;
					break;
				}
			}

			if (user == null) { //didn't find by password or network.
				throw new BadCredentialsException("Wrong password");
			}

			if (user.networkId == 0) {
				if (network != null) {
					user.networkId = network.id;
					userRepository.save(user);
				}
			}

			return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
		}

		throw new BadCredentialsException("Wrong username");
	}
}

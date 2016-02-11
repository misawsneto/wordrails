package co.xarx.trix.services;

import co.xarx.trix.domain.User;
import co.xarx.trix.domain.UserGrantedAuthority;
import co.xarx.trix.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(s);

		if(user == null)
			throw new UsernameNotFoundException("Username doesn't exist");

		return user;
	}

	public User create(String username, String password) {
		User user = new User();
		user.enabled = true;
		user.username = username;
		user.password = password;
		UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.USER);
		authority.user = user;
		user.addAuthority(authority);

		userRepository.save(user);

		return user;
	}
}
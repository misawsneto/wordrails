package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.persistence.UserRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class CacheService {
	@Autowired
	NetworkRepository networkRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	UserRepository userRepository;
	private LoadingCache<Integer, Person> persons;
	private LoadingCache<Integer, Network> networks;
	private LoadingCache<String, Person> persons2;
	private LoadingCache<String, Network> networks2;
	private LoadingCache<String, Network> networks3;
	private LoadingCache<Integer, Station> stations;
	private LoadingCache<String, User> user;
	@Autowired
	private PersonRepository personRepository;

	public void init() {
		// ------------- init person cache
		persons = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(1, TimeUnit.MINUTES)
				.build(new CacheLoader<Integer, Person>() {
							public Person load(Integer id) {
								return personRepository.findOne(id);
							}
						});

		// ------------- init network cache

		networks = CacheBuilder.newBuilder().maximumSize(500).expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(new CacheLoader<Integer, Network>() {
							public Network load(Integer id) {
								return networkRepository.findOne(id);
							}
						});
		// ------------- init person cache
		persons2 = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(1, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Person>() {
							public Person load(String username) {
								QUser user = QPerson.person.user;
								return personRepository.findOne(user.username.eq(username).and(user.enabled.eq(true)));
							}
						});

		// ------------- init user cache
		user = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, User>() {
			public User load(String username) {
				return userRepository.findOne(QUser.user.username.eq(username).and(QUser.user.enabled.eq(true)));
			}
		});

		// ------------- init network cache

		networks2 = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(new CacheLoader<String, Network>() {
							public Network load(String subdomain) {
								return networkRepository.findNetworkBySubdomain(subdomain);
							}
						});

		networks3 = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(new CacheLoader<String, Network>() {
							public Network load(String host) {
								return networkRepository.findByDomain(host);
							}
						});

		stations = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(new CacheLoader<Integer, Station>() {
							public Station load(Integer id) {
								return stationRepository.findOne(id);
							}
						});
	}

	public void updateStation(Integer id) {
		stations.refresh(id);
	}

	public Person getPersonByUsername(String username) throws ExecutionException {
		return persons2.get(username);
	}

	public User getUserByUsername(String username) throws ExecutionException {
		return user.get(username);
	}

	public void updatePerson(Integer id) {
		persons.refresh(id);
	}

	public void updatePerson(String username) {
		persons2.refresh(username);
	}

	public Network getNetwork(Integer id) {
		try {
			return networks.get(id);
		} catch (ExecutionException e) {
			return networkRepository.findOne(id);
		}
	}

	public void updateNetwork(String subdomain) {
		networks2.refresh(subdomain);
	}

	public void updateNetwork(Integer id) {
		networks.refresh(id);
	}

	public Network getNetworkBySubdomain(String subdomain) throws ExecutionException {
		return networks2.get(subdomain);
	}

	public Network getNetworkByDomain(String host) throws ExecutionException {
		return networks3.get(host);
	}

	public void removeNetwork(Integer id) {
		networks.invalidate(id);
	}
}

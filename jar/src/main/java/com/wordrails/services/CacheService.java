package com.wordrails.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.Station;
import com.wordrails.business.User;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class CacheService {
	private LoadingCache<Integer, Person> persons;
	private LoadingCache<Integer, Network> networks;

	private LoadingCache<String, Set<Person>> persons2;
	private LoadingCache<String, Network> networks2;

	private LoadingCache<Integer, Station> stations;

	private LoadingCache<Integer, User> user;

	private LoadingCache<String, Set<User>> users;

	@Autowired
	NetworkRepository networkRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PersonRepository personRepository;
	
	public void init(){
		// ------------- init person cache
		persons = CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<Integer, Person>() {
							public Person load(Integer id) {
								return personRepository.findOne(id);
							}
						});

		// ------------- init network cache

		networks = CacheBuilder.newBuilder().maximumSize(500)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<Integer, Network>() {
							public Network load(Integer id) {
								return networkRepository.findOne(id);
							}
						});
		// ------------- init person cache
		persons2 = CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
						//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<String, Set<Person>>() {
							public Set<Person> load(String username) {
								return personRepository.findByUsername(username);
							}
						});

		// ------------- init user cache
		user = CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				.build(new CacheLoader<Integer, User>() {
					public User load(Integer id) {
						User user = userRepository.findOne(id);
						return user;
					}
				});

		// ------------- init user cache
		users = CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Set<User>>() {
					public Set<User> load(String username) {
						Set<User> users = userRepository.findByUsernameAndEnabled(username, true);
						return users;
					}
				});

		// ------------- init network cache

		networks2 = CacheBuilder.newBuilder().maximumSize(100)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<String, Network>() {
							public Network load(String subdomain) {
								return networkRepository.findNetworkBySubdomain(subdomain);
							}
						});
		
		stations = CacheBuilder.newBuilder().maximumSize(100)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<Integer, Station>() {
							public Station load(Integer id) {
								return stationRepository.findOne(id);
							}
						});
		
	}
	
	public Station getStation(Integer id) throws ExecutionException{
		return stations.get(id);
	}
	
	public void updateStation(Integer id){
		stations.refresh(id);
	}
	
	public Person getPerson(Integer id) throws ExecutionException{
		return persons.get(id);
	}

	public Set<Person> getPersonsByUsername(String username) throws ExecutionException {
		return persons2.get(username);
	}

	public Person getPersonByUsernameAndNetworkId(String username, Integer networkId) throws ExecutionException {
		Set<Person> persons = persons2.get(username);

		for (Person p : persons) {
			if((p.user == null && networkId == 0) || p.user != null && p.user.network != null && Objects.equals(p.user.network.id, networkId))
				return p;
		}

		return null;
	}

	public Set<User> getUsersByUsername(String username) throws ExecutionException {
		return users.get(username);
	}

	public User getUser(Integer id) throws ExecutionException{
		return user.get(id);
	}

	public User getUserByUsernameAndNetworkId(String username, Integer networkId) throws ExecutionException {
		Set<User> us = users.get(username);

		for (User u : us) {
			if(u.network != null && Objects.equals(networkId, u.network.id))
				return u;
		}

		return null;
	}
	
	public void updatePerson(Integer id){
		persons.refresh(id);
	}
	
	public void updatePerson(String username){
		persons2.refresh(username);
	}
	
	public Network getNetwork(Integer id) throws ExecutionException{
		return networks.get(id);
	}
	
	public void updateNetwork(String subdomain){
		networks2.refresh(subdomain);
	}
	
	public void updateNetwork(Integer id){
		networks.refresh(id);
	}

	public Network getNetworkBySubdomain(String subdomain) throws ExecutionException {
		return networks2.get(subdomain);
	}
	
}

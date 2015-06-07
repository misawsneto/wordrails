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
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PersonRepository;

@Component
public class CacheService {
	private LoadingCache<Integer, Person> persons;
	private LoadingCache<Integer, Network> networks;
	
	private LoadingCache<String, Person> persons2;
	private LoadingCache<String, Network> networks2;

	@Autowired
	private PersonRepository personRepository; 

	@Autowired NetworkRepository networkRepository;

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
		persons2 = CacheBuilder.newBuilder().maximumSize(10000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<String, Person>() {
							public Person load(String username) {
								return personRepository.findByUsername(username);
							}
						});

		// ------------- init network cache

		networks2 = CacheBuilder.newBuilder().maximumSize(5000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<String, Network>() {
							public Network load(String subdomain) {
								return networkRepository.findNetworkBySubdomain(subdomain);
							}
						});
	}
	
	public Person getPerson(Integer id) throws ExecutionException{
		return persons.get(id);
	}
	
	public Network getNetwork(Integer id) throws ExecutionException{
		return networks.get(id);
	}

	public Network getNetworkBySubdomain(String subdomain) throws ExecutionException {
		return networks2.get(subdomain);
	}
	
	public Person getPersonByUsername(String username) throws ExecutionException {
		return persons2.get(username);
	}
}

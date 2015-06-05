package com.wordrails.services;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;

@Component
public class CacheService {
	CacheBuilder<Object, Object> persons;
	
	
}

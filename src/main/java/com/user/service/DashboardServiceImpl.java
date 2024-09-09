package com.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.dto.QuoteApiResponseDTO;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	private String quoteApiUrl = "https://dummyjson.com/quotes/random";

	@Override
	public QuoteApiResponseDTO getQuote() {
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<QuoteApiResponseDTO> forEntity = restTemplate.getForEntity(quoteApiUrl, QuoteApiResponseDTO.class);
		
		QuoteApiResponseDTO body = forEntity.getBody();
		
		return body;
	}

}

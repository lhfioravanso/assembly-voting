package com.lhfioravanso.assemblyvoting.integration;

import com.lhfioravanso.assemblyvoting.exception.BusinessException;
import com.lhfioravanso.assemblyvoting.exception.IntegrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class CpfService {
    private final RestTemplate restTemplate;
    private final Environment environment;

    private final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    @Autowired
    public CpfService(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    public boolean isAbleToVote(String cpf) {
        try {
            String url = environment.getProperty("cpf.service.url") + cpf;
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            return response.getBody().equals(ABLE_TO_VOTE);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode()== HttpStatus.NOT_FOUND)
                throw new BusinessException("Invalid CPF!");

            throw new IntegrationException("An error occurred while trying to access 'CpfService'.");
        }
    }

}

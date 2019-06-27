package com.ppegelow.messagerelay;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GreetCaller {
  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Async
  public void greetCall(String url) {
    RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(5)).build();
    ResponseEntity<?> result = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
    if(!result.getStatusCode().is2xxSuccessful()) {
      logger.error("Failed to greet: " + url);
    }
  }

}

package com.ppegelow.messagerelay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableAsync
public class MessageApp {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private GreetCaller greetCaller;

    String[] servicesToGreet = new String[]{
        "localhost:9000",
        //"sgreenberg",
        //"bwilliams",
        //"lcornell",
        //"cerickson"
    };

    String myServiceName = "ppegelow";

    public static void main(String[] args) {
        SpringApplication.run(MessageApp.class, args);
    }

    @RequestMapping(path = "/greet", method = RequestMethod.POST)
    public ResponseEntity<?> greet(
        @RequestParam(name = "greets") int greets,
        @RequestHeader(name = "referer", required = false) String referer) {

        logger.info("I, {}, was greeted by {}; {} greets left", myServiceName, referer != null ? referer : "mystery", greets);

        if (greets == 0) {
            logger.info("I was the last line of greeters: " + myServiceName);
            return ResponseEntity.ok().build();
        }
        greets--;

        RestTemplate restTemplate = restTemplateBuilder.build();
        for (String service: servicesToGreet) {
            greetCaller.greetCall(String.format("http://%s/greet?greets=%d", service, greets));
        }
        return ResponseEntity.ok().build();
    }
}

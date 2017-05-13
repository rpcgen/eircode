package com.fexco.eircode.controller;

import com.fexco.alliesComputing.AddressClient;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/v1")
public class EircodeController {

    private AddressClient addressClient;

    @Autowired
    public void setAddressClient(AddressClient addressClient) {
        this.addressClient = addressClient;
    }

    @Value("alliesComputing.apiKey")
    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Cacheable
    @RequestMapping(
            path     = "/get-address/{alpha2}/{fragment}",
            method   = GET,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAddress(
            @PathParam("alpha2")   String alpha2,
            @PathParam("fragment") String fragment,
            HttpServletRequest httpServletRequest) throws URISyntaxException {

        if (isCountrySupported(alpha2))
            return forwardRequestToAlliesComputing(alpha2, fragment, forceJsonFormat(httpServletRequest.getParameterMap()));
        else
            return ResponseEntity.notFound().build();

    }

    private Map<String, String[]> forceJsonFormat(Map<String, String[]> params) {
        Map<String, String[]> result = params == null ? new HashMap<>() : new HashMap<>(params);
        result.put("format", new String[] { "json" });
        return result;
    }

    private boolean isCountrySupported(String alpha2) {
        return "uk".equals(alpha2) || "ie".equals(alpha2);
    }

    private ResponseEntity<?> forwardRequestToAlliesComputing(
            String alpha2,
            String fragment,
            Map<String, String[]> params) throws URISyntaxException {

        return addressClient.forwardRequestToAlliesComputing(alpha2, fragment, params);
    }
}

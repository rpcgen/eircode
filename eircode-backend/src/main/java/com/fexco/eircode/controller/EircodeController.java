package com.fexco.eircode.controller;

import com.fexco.alliesComputing.AddressClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/v1")
public class EircodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EircodeController.class);

    private AddressClient addressClient;

    @Autowired
    public void setAddressClient(AddressClient addressClient) {
        this.addressClient = addressClient;
    }

    @RequestMapping(
            path     = "/get-address/{alpha2}/{fragment}",
            method   = GET,
            produces = APPLICATION_JSON_VALUE)

    public ResponseEntity<?> getAddress(
            @PathVariable("alpha2")   String alpha2,
            @PathVariable("fragment") String fragment,
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

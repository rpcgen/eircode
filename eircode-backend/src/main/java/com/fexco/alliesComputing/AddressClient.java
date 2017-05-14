package com.fexco.alliesComputing;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

@Component
@CacheConfig(cacheNames = "address-cache")
public class AddressClient {

    private static final Logger LOG = LoggerFactory.getLogger(AddressClient.class);

    private static final String PATH_FORMAT = "/pcw/%s/address/%s/%s";

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String scheme;

    @Value("${alliesComputing.scheme:http}")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    private String host;

    @Value("${alliesComputing.host:ws.postcoder.com}")
    public void setHost(String host) {
        this.host = host;
    }

    private String apiKey;

    @Value("${alliesComputing.apiKey}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Cacheable(keyGenerator= "addressKeyGenerator")
    public ResponseEntity<String> forwardRequestToAlliesComputing(
            String alpha2,
            String fragment,
            Map<String, String[]> params) throws URISyntaxException {

        URIBuilder uriBuilder = new URIBuilder()
                .setScheme(scheme)
                .setHost  (host)
                .setPath  (String.format(PATH_FORMAT, apiKey, alpha2, fragment));

        for (Entry<String, String[]> param : params.entrySet())
            for (String value : param.getValue())
                uriBuilder.addParameter(param.getKey(), value);

        String url = uriBuilder.build().toString();

        LOG.info(url);
        return restTemplate.getForEntity(uriBuilder.build().toString(), String.class);
    }
}

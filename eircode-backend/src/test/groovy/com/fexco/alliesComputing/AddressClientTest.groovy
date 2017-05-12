package com.fexco.alliesComputing

import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class AddressClientTest extends Specification {

    void "address client must use http client to perform the GET request to allies computing"() {

        given:
        RestTemplate restTemplate = Mock(RestTemplate)

        AddressClient addressClient = new AddressClient()
        addressClient.apiKey = 'apiKey'
        addressClient.scheme = 'http'
        addressClient.host   = 'localhost'
        addressClient.restTemplate = restTemplate

        when:
        addressClient.forwardRequestToAlliesComputing('ie', 'zip', Collections.emptyMap())

        then:
        1 * restTemplate.getForEntity(_ as String, String)
    }

    void "address client must build a proper url using all params"() {

        given:
        RestTemplate restTemplate = Mock(RestTemplate)

        AddressClient addressClient = new AddressClient()
        addressClient.apiKey = 'apiKey'
        addressClient.scheme = 'http'
        addressClient.host   = 'localhost'
        addressClient.restTemplate = restTemplate

        Map<String, String[]> options = new HashMap<>();
        options.put('format', ['json'] as String[]);

        when:
        addressClient.forwardRequestToAlliesComputing('ie', 'zip', options)

        then:
        1 * restTemplate.getForEntity('http://localhost/pcw/apiKey/address/ie/zip?format=json', String)
    }
}

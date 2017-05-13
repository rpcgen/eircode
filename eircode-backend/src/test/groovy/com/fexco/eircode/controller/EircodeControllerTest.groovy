package com.fexco.eircode.controller

import com.fexco.alliesComputing.AddressClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class EircodeControllerTest extends Specification {

    void "controller must forward the request to the client"() {

        given:
        AddressClient addressClient = Mock(AddressClient)

        EircodeController controller = new EircodeController()
        controller.addressClient = addressClient

        when:
        controller.getAddress('uk', 'fragment', Mock(HttpServletRequest))

        then:
        1 * addressClient.forwardRequestToAlliesComputing('uk', 'fragment', _ as Map)
    }

    void "controller won't forward the request when country is not supported returning 404 status code"() {

        given:
        AddressClient addressClient = Mock(AddressClient)

        EircodeController controller = new EircodeController()
        controller.addressClient = addressClient

        when:
        ResponseEntity<?> response = controller.getAddress('xx', 'fragment', Mock(HttpServletRequest))

        then:
        0 * addressClient.forwardRequestToAlliesComputing(_ as String, _ as String, _ as Map)
        response.statusCode == HttpStatus.NOT_FOUND
    }

    void "controller must force json format"() {

        given:
        AddressClient addressClient = Mock(AddressClient)
        HttpServletRequest httpServletRequest = Mock(HttpServletRequest)
        httpServletRequest.getParameterMap() >> ['format' : ['xml'] as String[]]

        EircodeController controller = new EircodeController()
        controller.addressClient = addressClient

        when:
        ResponseEntity<?> response = controller.getAddress('uk', 'fragment', httpServletRequest)

        then:
        1 * addressClient.forwardRequestToAlliesComputing('uk', 'fragment', { Map<String, String[]> z -> z.format[0] == 'json' })
    }
}

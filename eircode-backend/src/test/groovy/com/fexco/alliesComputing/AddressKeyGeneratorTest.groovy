package com.fexco.alliesComputing

import spock.lang.Specification

class AddressKeyGeneratorTest extends Specification {

    void "key must be an String"() {

        expect:
        new AddressKeyGenerator().generate(null, null, null, null, null) instanceof String
    }

    void "key must include the country code and expect it as the first parameter"() {

        String key = new AddressKeyGenerator().generate(null, null, 'ie', null, null) as String

        expect:
        key.contains('alpha2=ie')
    }

    void "key must include the address fragment and expect it as the second parameter"() {

        String key = new AddressKeyGenerator().generate(null, null, null, 'zip', null) as String

        expect:
        key.contains('fragment=zip')
    }

    void "key must include the options and expect it as the second third"() {

        String key = new AddressKeyGenerator().generate(null, null, null, null, Collections.emptyMap()) as String

        expect:
        key.contains('options={}')
    }

    void "when no enough argument are present generator must throw an illegal argument exception"() {

        when:
        new AddressKeyGenerator().generate(null, null);

        then:
        thrown(IllegalArgumentException)
    }
}

package com.fexco.alliesComputing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class AddressKeyGenerator implements KeyGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(AddressKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, Object... params) {

        if (params == null || params.length < 3)
            throw new IllegalArgumentException("Expected four params to generate a proper key");

        Map<String, Object> key = new HashMap<>();
        key.put("alpha2",   params[0]);
        key.put("fragment", params[1]);
        key.put("options",  transform(params[2]));

        LOG.info("using key {}", key.toString());

        return key.toString();
    }

    private Object transform(Object param) {

        if (param == null)
            return null;

        Map<String, String> result = new HashMap<>();

        ((Map<String, String[]>) param)
                .entrySet()
                .forEach((Map.Entry<String, String[]> x) -> result.put(x.getKey(), Arrays.toString(x.getValue())));

        return result;
    }
}

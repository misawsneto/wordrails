package com.wordrails.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.WordpressConfig;
import java.io.IOException;

/**
 *
 * @author arthur
 */
public class Test {
    
    public static void main(String[] args) throws IOException {
        String rawJson = "{\"user\":\"trix_user\",\"password\":false,\"domain\":\"http:\\/\\/taqueo.pe.hu\\/wp-json\",\"token\":\"123999a2022ada6dbc4adc31dfceb987\",\"terms\":{\"category\":[{\"ID\":\"8\",\"name\":\"teste nova categoria\",\"slug\":\"teste-nova-categoria\",\"taxonomy\":\"category\",\"parent\":\"0\"},{\"ID\":\"1\",\"name\":\"Uncategorized\",\"slug\":\"uncategorized\",\"taxonomy\":\"category\",\"parent\":\"0\"}],\"post_tag\":[{\"ID\":\"14\",\"name\":\"312312\",\"slug\":\"312312\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"13\",\"name\":\"dadasd\",\"slug\":\"dadasd\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"11\",\"name\":\"dasdas\",\"slug\":\"dasdas\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"18\",\"name\":\"dasdsa\",\"slug\":\"dasdsa\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"12\",\"name\":\"ddddddddd\",\"slug\":\"ddddddddd\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"15\",\"name\":\"dsaad\",\"slug\":\"dsaad\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"16\",\"name\":\"dsads\",\"slug\":\"dsads\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"9\",\"name\":\"tag1\",\"slug\":\"tag1\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"10\",\"name\":\"tag2\",\"slug\":\"tag2\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"4\",\"name\":\"teste1\",\"slug\":\"teste1\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"},{\"ID\":\"5\",\"name\":\"teste2\",\"slug\":\"teste2\",\"taxonomy\":\"post_tag\",\"parent\":\"0\"}]}}";
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        WordpressConfig posts = mapper.readValue(rawJson, WordpressConfig.class);
    }
}

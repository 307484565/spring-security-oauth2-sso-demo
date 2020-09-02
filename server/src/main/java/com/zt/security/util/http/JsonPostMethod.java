package com.zt.security.util.http;

import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class JsonPostMethod extends BasePostMethod<Object> {

    private Object body;

    public JsonPostMethod(SimpleClientHttpRequestFactory factory, RestTemplate restTemplate, MediaType mimeType, String url, Object... uriVariables) {
        super(factory, restTemplate, mimeType, url, uriVariables);
    }

    @Override
    Object setBody() {
        return body;
    }

    @Override
    public JsonPostMethod addHeader(String name, String value) {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public JsonPostMethod addHeader(Map<String, ?> headers) {
        super.addHeader(headers);
        return this;
    }

    public <T> JsonPostMethod setBody(T body) {
        this.body = body;
        return this;
    }

}

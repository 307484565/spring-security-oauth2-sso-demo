package com.zt.security.util.http;

import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class BasePostMethod<B> {
    protected String url;
    protected final RestTemplate restTemplate;
    protected final SimpleClientHttpRequestFactory factory;

    protected final HttpHeaders headers = new HttpHeaders();
    protected B body;
    protected final Object[] uriVariables;

    public BasePostMethod(SimpleClientHttpRequestFactory factory, RestTemplate restTemplate, MediaType mimeType, String url, Object... uriVariables) {
        headers.setContentType(mimeType);
        this.factory = factory;
        this.restTemplate = restTemplate;
        this.url = url;
        this.uriVariables = uriVariables;
    }

    public BasePostMethod<B> addHeader(String name, String value) {
        headers.add(name, value);
        return this;
    }

    public BasePostMethod<B> addHeader(Map<String, ?> headers) {
        headers.forEach((k, v) -> this.headers.add(k, v.toString()));
        return this;
    }

    /**
     * 设置读取超时，默认 5000
     * @see HttpUtil#READ_TIMEOUT
     * @param readTimeout
     * @return
     */
    public BasePostMethod<B> setReadTimeout(int readTimeout) {
        factory.setReadTimeout(readTimeout);
        return this;
    }

    /**
     * 设置连接超时，默认 5000
     * @see HttpUtil#CONNECT_TIMEOUT
     * @param connectTimeout
     * @return
     */
    public BasePostMethod<B> setConnectTimeout(int connectTimeout) {
        factory.setConnectTimeout(connectTimeout);
        return this;
    }

    abstract B setBody();

    public <T> ResponseEntity<T> executeForEntity(Class<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(setBody(), headers), clazz, uriVariables);
    }

    /**
     * 执行、返回指定类
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T execute(Class<T> clazz) {
        setBody();
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(setBody(), headers), clazz, uriVariables);
        return exchange.getBody();
    }

    /**
     * 直接返回响应字符串
     * @return response
     */
    public String execute() {
        setBody();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(setBody(), headers), String.class, uriVariables);
        return exchange.getBody();
    }





}

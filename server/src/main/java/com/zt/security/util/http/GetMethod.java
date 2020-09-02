package com.zt.security.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMethod {
    private static final Logger logger = LoggerFactory.getLogger(GetMethod.class);

    private String url;
    private final RestTemplate restTemplate;
    private final SimpleClientHttpRequestFactory factory;

    private final MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
    private final Map<String, String> queryParamMap = new HashMap<>();
    private final Object[] uriVariables;

    public GetMethod(SimpleClientHttpRequestFactory factory, RestTemplate restTemplate, String url, Object... uriVariables) {
        this.factory = factory;
        this.restTemplate = restTemplate;
        this.url = url;
        this.uriVariables = uriVariables;
    }

    public GetMethod addHeader(String name, String value) {
        headerMap.add(name, value);
        return this;
    }

    public GetMethod addHeaders(Map<String, ?> headers) {
        headers.forEach((k, v) -> headerMap.add(k, v.toString()));
        return this;
    }

    /**
     * 拼接URL = url?key=value&...
     * @param key
     * @param value
     * @return
     */
    public GetMethod addQueryParam(String key, String value) {
        queryParamMap.put(key, value);
        return this;
    }

    /**
     * 拼接URL = url?name=value&...
     * @param params url参数map
     * @return
     */
    public GetMethod addQueryParams(Map<String, ?> params) {
        params.forEach((k, v) -> queryParamMap.put(k, v.toString()));
        return this;
    }

    /**
     * 设置读取超时，默认 5000
     * @see HttpUtil#READ_TIMEOUT
     * @param readTimeout
     * @return
     */
    public GetMethod setReadTimeout(int readTimeout) {
        factory.setReadTimeout(readTimeout);
        return this;
    }

    /**
     * 设置连接超时，默认 5000
     * @see HttpUtil#CONNECT_TIMEOUT
     * @param connectTimeout
     * @return
     */
    public GetMethod setConnectTimeout(int connectTimeout) {
        factory.setConnectTimeout(connectTimeout);
        return this;
    }

    public <T> ResponseEntity<T> executeForEntity(Class<T> clazz) {
        jointUrl();
        return exec(url, HttpMethod.GET, new HttpEntity<>(headerMap), clazz, uriVariables);
    }

    /**
     * 执行、返回指定类
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T execute(Class<T> clazz) {
        jointUrl();
        return exec(url, HttpMethod.GET, new HttpEntity<>(headerMap), clazz, uriVariables).getBody();
    }

    /**
     * 直接返回响应字符串
     * @return response
     */
    public String execute() {
        jointUrl();
        return exec(url, HttpMethod.GET, new HttpEntity<>(headerMap), String.class, uriVariables).getBody();
    }

    private <R, E> ResponseEntity<R> exec(String url, HttpMethod method, HttpEntity<E> httpEntity, Class<R> responseType, Object... uriVariables) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ResponseEntity<R> exchange = null;
        try {
            exchange = restTemplate.exchange(url, method, httpEntity, responseType, uriVariables);
            return exchange;
        } catch (Exception e) {
            logger.error("请求异常, msg={}", e.getMessage());
            throw e;
        } finally {
            stopWatch.stop();
            String response = "";
            if(exchange != null && exchange.getBody() != null) {
                response = exchange.getBody().toString();
            }
            logger.info("请求完成 useTime={}ms, url={}, request={}, response={}", stopWatch.getTotalTimeMillis(), url,
                    httpEntity.toString(), response);
        }
    }

    private void jointUrl() {
        StringBuilder sb = new StringBuilder();
        List<String> keys = Arrays.asList(queryParamMap.keySet().toArray(new String[]{}));
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            Object valueObj = queryParamMap.get(key);
            if(valueObj == null) {
                continue;
            }
            sb.append(key).append("=").append(valueObj.toString());
            if(i != keys.size() - 1) {
                sb.append("&");
            }
        }
        String jointStr = sb.toString();
        if(!StringUtils.isEmpty(jointStr)) {
            url = url.endsWith("?") ? url + sb.toString() : url + "?" + sb.toString();
        }
    }

}

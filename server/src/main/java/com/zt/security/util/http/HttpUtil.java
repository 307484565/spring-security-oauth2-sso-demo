package com.zt.security.util.http;

import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpUtil {
    public static final int READ_TIMEOUT = 5000;
    public static final int CONNECT_TIMEOUT = 5000;
    private static final SimpleClientHttpRequestFactory FACTORY = new SimpleClientHttpRequestFactory();

    private HttpUtil() {}

    private static class RestTemplateHolder {
        public static final RestTemplate restTemplate;
        static {
            FACTORY.setReadTimeout(READ_TIMEOUT);
            FACTORY.setConnectTimeout(CONNECT_TIMEOUT);
            restTemplate = new RestTemplate(FACTORY);
            List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
            messageConverters.forEach(httpMessageConverter -> {
                if(httpMessageConverter instanceof StringHttpMessageConverter){
                    StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                    messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
                }
            });
        }
    }

    public static RestTemplate getRestTemplate() {
        return RestTemplateHolder.restTemplate;
    }

    /**
     * 构建GET请求
     * @param url
     * @return
     */
    public static GetMethod buildGet(String url) {
        return new GetMethod(FACTORY, getRestTemplate(), url);
    }

    /**
     * 构建application/json POST请求
     * @param url
     * @return
     */
    public static JsonPostMethod buildJsonPost(String url, Object... uriVariables) {
        return new JsonPostMethod(FACTORY, getRestTemplate(), MediaType.APPLICATION_JSON, url, uriVariables);
    }

    /**
     * 构建application/x-www-form-urlencoded 表单POST请求
     * @param url
     * @return
     */
    public static FormPostMethod buildFormPost(String url, Object... uriVariables) {
        return new FormPostMethod(FACTORY, getRestTemplate(), MediaType.APPLICATION_FORM_URLENCODED, url, uriVariables);
    }

    /**
     * 构建GET请求，附加URL路径参数
     * 如 url = http://www.xxx.com/users/{id}/kids/{name} uriVariables=[id, name]
     * @param url 带参URL
     * @param uriVariables url参数数组
     * @return
     */
    public static GetMethod buildGet(String url, Object... uriVariables) {
        return new GetMethod(FACTORY, getRestTemplate(), url, uriVariables);
    }

}

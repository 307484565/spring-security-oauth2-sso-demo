package com.zt.security.util.http;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class FormPostMethod extends BasePostMethod<MultiValueMap<String, Object>>{
    protected final MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();

    public FormPostMethod(SimpleClientHttpRequestFactory factory, RestTemplate restTemplate, MediaType mimeType, String url, Object... uriVariables) {
        super(factory, restTemplate, mimeType, url, uriVariables);
    }

    public FormPostMethod setReadTimeout(int readTimeout) {
        super.setReadTimeout(readTimeout);
        return this;
    }

    public FormPostMethod setConnectTimeout(int connectTimeout) {
        super.setConnectTimeout(connectTimeout);
        return this;
    }

    @Override
    public <T> ResponseEntity<T> executeForEntity(Class<T> clazz) {
        super.body = paramMap;
        return super.executeForEntity(clazz);
    }

    @Override
    public FormPostMethod addHeader(String name, String value) {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public FormPostMethod addHeader(Map<String, ?> headers) {
        super.addHeader(headers);
        return this;
    }

    @Override
    protected MultiValueMap<String, Object> setBody() {
        return paramMap;
    }

    /**
     * 添加表单参数
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> FormPostMethod addParam(String key, T value) {
        paramMap.add(key, value);
        return this;
    }

    /**
     * 批量添加表单参数，支持bean或map
     * @param param
     * @param <T>
     * @return
     */
    public <T> FormPostMethod addParams(T param) {
        Map<String, Object> map;
        if (param instanceof Map) {
            map = (Map) param;
        } else {
            map = BeanMap.create(param);
        }
        map.forEach(paramMap::add);
        return this;
    }


}

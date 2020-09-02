package com.zt.security;

import com.zt.security.util.http.HttpUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class TestMain {

    public static final String DOMAIN = "http://localhost:9000/oauth/";
    public static final String PATH_AUTHORIZE = DOMAIN + "authorize";
    public static final String PATH_TOKEN = DOMAIN + "token";
    public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    public static final String GRANT_TYPE_PASSOWRD = "password";
    public static final String GRANT_TYPE_IMPLICIT = "implicit";
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String RESPONSE_TYPE_CODE = "code";
    public static final String RESPONSE_TYPE_TOKEN = "token";
    public static final String REDIRECT_URI = "http://zt307.xyz/callback";

    /**
     * 一： 生成授权链接
     * @param args
     */
    public static void main(String[] args) {
//        firstBuildAuthorizeUrl();
//        secondRequestToken();
//        thirdRequestResource();

    }

    static void firstBuildAuthorizeUrl() {
        //授权码
//        http://localhost:9000/oauth/authorize?grant_type=authorization_code&scope=all&response_type=code&redirect_uri=http://zt307.xyz/callback&client_id=client1
        HashMap<String, Object> params = new HashMap<>();
        params.put("client_id", "client1");
        params.put("grant_type", GRANT_TYPE_AUTHORIZATION_CODE);
        params.put("response_type", RESPONSE_TYPE_CODE);
        params.put("redirect_uri", REDIRECT_URI);
        params.put("scope", "all");
        String url = jointQueryParams(PATH_AUTHORIZE, params);
        System.out.println(url);

        //隐式授权， 跳转时直接带token
//        http://zt307.xyz/callback#access_token=IPQpuNF1Iq8P6yA5Zo06GOlZ7Ro&token_type=bearer&expires_in=2592000
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("client_id", "client2");
//        params.put("grant_type", GRANT_TYPE_IMPLICIT);
//        params.put("response_type", RESPONSE_TYPE_TOKEN);
//        params.put("redirect_uri", REDIRECT_URI);
//        params.put("scope", "all");
//        String url = jointQueryParams(PATH_AUTHORIZE, params);
//        System.out.println(url);
    }

    /**
     * 三： 请求token
     */
    static void secondRequestToken() {
        //授权码模式
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", "OH96_8");
        params.put("client_id", "client1");
        params.put("client_secret", "123");
        params.put("grant_type", GRANT_TYPE_AUTHORIZATION_CODE);
        params.put("response_type", RESPONSE_TYPE_CODE);
        params.put("redirect_uri", REDIRECT_URI);
        params.put("scope", "all");
        String execute = HttpUtil.buildFormPost(PATH_TOKEN).addParams(params).execute();
        System.out.println(execute);

        //密码授权模式
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("client_id", "client1");
//        params.put("client_secret", "123");
//        params.put("grant_type", GRANT_TYPE_PASSOWRD);
//        params.put("response_type", RESPONSE_TYPE_TOKEN);
//        //用户的账号密码
//        params.put("username", "admin");
//        params.put("password", "123456");
//        params.put("scope", "all");
//        String execute = HttpUtil.buildFormPost(PATH_TOKEN).addParams(params).execute();
//        System.out.println(execute);

        //客户端授权模式
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("client_id", "client3");
//        params.put("client_secret", "123");
//        params.put("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS);
//        //根据实际选择
//        params.put("scope", "all");
//        String execute = HttpUtil.buildFormPost(PATH_TOKEN).addParams(params).execute();
//        System.out.println(execute);

    }

    static void thirdRequestResource() {
        String rsp = HttpUtil.buildGet("http://localhost:9000/user/list")
                .addHeader("Authorization", "bearer ZA567DSj5txagIXIvErWvxtJWdA").execute();
        System.out.println(rsp);
    }

    public static String jointQueryParams(String path, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = Arrays.asList(params.keySet().toArray(new String[]{}));
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            Object valueObj = params.get(key);
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
            path = path.endsWith("?") ? path + sb.toString() : path + "?" + sb.toString();
        }
        return path;
    }

}

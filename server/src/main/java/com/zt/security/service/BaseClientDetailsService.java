package com.zt.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义客户端认证
 *
 * @author zhangtao
 */
@Slf4j
@Component
public class BaseClientDetailsService implements ClientDetailsService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        System.out.println(clientId);
        BaseClientDetails client = null;
        //这里可以改为查询数据库
        if("client".equals(clientId)) {
            log.info(clientId);
            client = new BaseClientDetails();
            client.setClientId(clientId);
            client.setClientSecret("123");
            //client.setResourceIds(Arrays.asList("order"));
            client.setAuthorizedGrantTypes(Arrays.asList("authorization_code",
                    "client_credentials", "refresh_token", "password", "implicit"));
            //不同的client可以通过 一个scope 对应 权限集
            client.setScope(Arrays.asList("all", "select"));
            client.setAuthorities(AuthorityUtils.createAuthorityList("admin_role"));
            client.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1)); //1天
            client.setRefreshTokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(1)); //1天
            Set<String> uris = new HashSet<>();
            uris.add("http://localhost:8000/login");
            client.setRegisteredRedirectUri(uris);
        }
        if(client == null) {
            throw new NoSuchClientException("No client width requested id: " + clientId);
        }
        return client;
    }

}


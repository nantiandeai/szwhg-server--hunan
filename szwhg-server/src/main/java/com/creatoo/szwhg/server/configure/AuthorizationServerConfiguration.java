package com.creatoo.szwhg.server.configure;

import com.creatoo.szwhg.base.service.DeviceClientDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Created by yunyan on 2017/8/15.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DeviceClientDetail clientService;

    @Autowired
    private UserDetailsService userService;
    @Value("${client.admin.id}")
    private String adminId;
    @Value("${client.admin.secret}")
    private String adminSecret;
    @Value("${client.pcweb.id}")
    private String pcwebId;
    @Value("${client.pcweb.secret}")
    private String pcwebSecret;
    @Value("${client.mobileweb.id}")
    private String mobilewebId;
    @Value("${client.mobileweb.secret}")
    private String mobilewebSecret;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(adminId).secret(adminSecret)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write").and()
                .withClient(pcwebId).secret(pcwebSecret)
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("read", "write").and()
                .withClient(mobilewebId).secret(mobilewebSecret)
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("read", "write")
        .and().clients(clientService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .tokenServices(tokenServices())
                .authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }

    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setAccessTokenValiditySeconds(-1);
        tokenServices.setRefreshTokenValiditySeconds(-1);
        tokenServices.setSupportRefreshToken(true); // support refresh token
        tokenServices.setTokenStore(tokenStore()); // use in-memory token store
        return tokenServices;
    }

}

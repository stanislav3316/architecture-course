@file:Suppress("DEPRECATION")

package com.uberpopug.employee.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.stereotype.Component

@Configuration
@EnableAuthorizationServer
class OAuth2AuthorizationServer(
    private val passwordEncoder: PasswordEncoder
) : AuthorizationServerConfigurerAdapter() {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var oauthServicesProperties: OAuthServicesProperties

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        oauthServicesProperties.clients.forEach { client ->
            clients
                .inMemory()
                .withClient(client.clientId)
                .secret(passwordEncoder.encode(client.clientSecret))
                .authorizedGrantTypes(client.authorizedGrandTypes)
                .scopes(client.scope)
                .autoApprove(client.approve.toBoolean())
                .redirectUris(client.redirectUrl)
        }
    }
}

@Component
@ConfigurationProperties(prefix = "oauth")
class OAuthServicesProperties(val clients: List<OAuthClient>)

class OAuthClient {
    lateinit var clientId: String
    lateinit var clientSecret: String
    lateinit var authorizedGrandTypes: String
    lateinit var scope: String
    lateinit var approve: String
    lateinit var redirectUrl: String
}

@file:Suppress("DEPRECATION")

package com.uberpopug.employee.controller

import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class OAuthController {

    @GetMapping("/user/me")
    fun profile(user: OAuth2Authentication): Principal {
        return user
    }
}

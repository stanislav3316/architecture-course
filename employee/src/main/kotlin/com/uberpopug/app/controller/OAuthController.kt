package com.uberpopug.app.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class OAuthController {

    @GetMapping("/user/me")
    fun profile(user: Principal): Principal {
        return user
    }
}

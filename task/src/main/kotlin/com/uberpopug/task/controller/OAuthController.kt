package com.uberpopug.task.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OAuthController {

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal user: OAuth2User) = user
}

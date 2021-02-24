package io.absurdlab.demojavaclient;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/oidc")
    public OidcUser getOidcUserPrincipal(@AuthenticationPrincipal OidcUser principal, Authentication auth) {
        return principal;
    }
}

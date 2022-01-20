/*

package com.puscas.authentication.util;

import com.fasterxml.jackson.databind.Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.jackson2.WebJackson2Module;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.Cookie;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedCookie;
public class WebServletJackson2Module extends SimpleModule {

    public WebServletJackson2Module() {
       // super(WebJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
        super(WebServletJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(Module.SetupContext context) {
        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
        context.setMixInAnnotations(Cookie.class, CookieMixin.class);
        context.setMixInAnnotations(SavedCookie.class, SavedCookieMixin.class);
        context.setMixInAnnotations(DefaultSavedRequest.class, DefaultSavedRequestMixin.class);
        context.setMixInAnnotations(WebAuthenticationDetails.class, WebAuthenticationDetailsMixin.class);
    }
}

*/

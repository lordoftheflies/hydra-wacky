/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.datasource;

import com.ge.current.innovation.TabDto;
import com.ge.current.innovation.UserInfoDto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lordoftheflies
 */
@RestController
@RequestMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ViewsController {

    private static final Logger LOG = Logger.getLogger(ViewsController.class.getName());

    @RequestMapping(path = "/oauth/authorize",
            method = RequestMethod.GET)
    public UserInfoDto authorize(
            @RequestParam("state") String state,
            @RequestParam("redirect_uri") String redirectUrl,
            @RequestParam("response_type") String responseType,
            @RequestParam("client_id") String clientId) {
        LOG.info("Get user information ...");
        LOG.log(Level.INFO, "- State: {0}", state);
        LOG.log(Level.INFO, "- Redirect-url: {0}", redirectUrl);
        LOG.log(Level.INFO, "- Response tpye: {0}", responseType);
        LOG.log(Level.INFO, "- Client-id: {0}", clientId);
        UserInfoDto dto = new UserInfoDto("hydra_user");
        dto.getDecks().add(new TabDto("fa-tachometer", "model", "Modeling"));
        dto.getDecks().add(new TabDto("fa-tachometer", "infrastructure", "infrastructure"));
        LOG.log(Level.INFO, "User information: {0}", dto.toString());
        return dto;
    }

    @RequestMapping(path = "/logout",
            method = RequestMethod.POST)
    public void getUserInfo(
            @RequestParam("redirect") String redirectUrl,
            HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", redirectUrl);
    }
}

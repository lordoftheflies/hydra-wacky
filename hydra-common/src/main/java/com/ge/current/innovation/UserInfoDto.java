/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lordoftheflies
 */
@XmlRootElement
public class UserInfoDto implements Serializable{

    public UserInfoDto() {
    }

    public UserInfoDto(String userName) {
        this.userName = userName;
    }
    
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    private List<TabDto> decks = new ArrayList<TabDto>();

    public List<TabDto> getDecks() {
        return decks;
    }

    public void setDecks(List<TabDto> decks) {
        this.decks = decks;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset;

import java.util.List;

/**
 *
 * @author predix
 */
public class Machine extends Edge {
    private List<Edge> childen;

    public List<Edge> getChilden() {
        return childen;
    }

    public void setChilden(List<Edge> childen) {
        this.childen = childen;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.dto;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@XmlRootElement
public class AssetRootDto extends AssetNodeDto implements Serializable {
    private List<AssetNodeDto> children;

    public List<AssetNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<AssetNodeDto> children) {
        this.children = children;
    }
    
    
}

package com.ge.lighting.innovation.persistence.ext;

import java.io.Serializable;

public abstract class AbstractEntity<Key> implements Serializable {

    public AbstractEntity() {
    }

    
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public abstract Key getId();

    public abstract void setId(Key id);

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[id=" + getId() + "]";
    }
}

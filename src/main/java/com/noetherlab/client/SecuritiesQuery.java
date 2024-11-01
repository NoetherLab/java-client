package com.noetherlab.client;

import java.util.Collection;
import java.util.HashSet;

public class SecuritiesQuery {
    Collection<String> securityIds = new HashSet<>();
    Collection<String> tags = new HashSet<>();
    Collection<String> indices = new HashSet<>();

    public Collection<String> getSecurityIds() {
        return securityIds;
    }

    public void setSecurityIds(Collection<String> securityIds) {
        this.securityIds = securityIds;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public Collection<String> getIndices() {
        return indices;
    }

    public void setIndices(Collection<String> indices) {
        this.indices = indices;
    }
}

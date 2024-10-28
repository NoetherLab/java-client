package com.noetherlab.client.model;

import java.time.LocalDateTime;

public class Model {
    LocalDateTime T;

    LocalDateTime fromT;

    public LocalDateTime getT() {
        return T;
    }

    public void setT(LocalDateTime t) {
        T = t;
    }

    public LocalDateTime getFromT() {
        return fromT;
    }
    public void setFromT(LocalDateTime fromT) {
        this.fromT = fromT;
    }

}


package com.cherryhouse.server._core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Cursor {

    private Long key;
    private Integer size;

    private static final Integer DEFAULT_SIZE = 10;

    public Boolean hasKey() {
        return key != null;
    }

    public Integer getSize(){
        return size == null ? DEFAULT_SIZE : size;
    }
}

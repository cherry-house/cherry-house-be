package com.cherryhouse.server.post;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    HAIR,
    NAIL,
    MAKEUP,
    ETC;

    @JsonCreator
    public static Category from(String s){ //enum을 request로 받기 위해서 역직렬화 구현
        if(isEmpty(s)) return null;
        return Category.valueOf(s.toUpperCase());
    }

    private static boolean isEmpty(String s) {
        return s == null || s.equals("") || s.equals(" ");
    }
}

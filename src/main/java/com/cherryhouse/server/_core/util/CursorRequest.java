package com.cherryhouse.server._core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CursorRequest {

    private Long key;
    private Integer size;
}

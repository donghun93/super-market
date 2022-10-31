package com.devwinter.supermarket.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorArgumentResponse {

    private String errorCode;
    private String errorMessage;
    private Map<String, String> errorMaps = new HashMap<>();
}

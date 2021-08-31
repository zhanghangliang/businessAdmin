package com.ob.common.businessAdmin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class CommonReqDTO {

    private String url;

    private Map<String, String> data;
}

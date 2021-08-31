package com.ob.common.businessAdmin.config;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class PreGetSwaggerFactory {

    private static final Logger LOG = Logger.getLogger(PreGetSwaggerFactory.class.getName());

    public static List<String> getMethodUrl = new ArrayList<>();

    public static List<String> postMethodUrl = new ArrayList<>();

    @PostConstruct
    public void preGetSwagger() {
        HttpResponse response = HttpUtil.createGet("http://server.dyzwdbk.com/gov-wiki-admin/v2/api-docs").execute();
        String pathsStr = JSONUtil.getByPath(JSONUtil.parse(response.body()), "$.paths").toString();
        JSONObject paths = JSONUtil.parseObj(pathsStr);
        paths.forEach((key, value) -> {
            String postRef = JSONUtil.getByPath(JSONUtil.parse(value), "$.post.responses.200.schema.$ref", "");
            String getRef = JSONUtil.getByPath(JSONUtil.parse(value), "$.get.responses.200.schema.$ref", "");
            String post = JSONUtil.getByPath(JSONUtil.parse(value), "$.post", "");
            String get = JSONUtil.getByPath(JSONUtil.parse(value), "$.get", "");

            if(!StringUtils.isEmpty(post)) {
                postMethodUrl.add(key);
            }
            if(!StringUtils.isEmpty(get)) {
                getMethodUrl.add(key);
            }

//            // 全部请求
//            System.out.println(key);
//            System.out.println(value);
//            System.out.println("----");

//            // 其中一种请求有返回值
//            if (!StringUtils.isEmpty(postRef) || !StringUtils.isEmpty(getRef)) {
//                System.out.println(key);
//                System.out.println(value);
//                System.out.println("----");
//            }

//            // 请求没有返回值
//            if (StringUtils.isEmpty(postRef) && StringUtils.isEmpty(getRef)) {
//                System.out.println(key);
//                System.out.println(value);
//                System.out.println("----");
//            }

            // 有get请求的
            if (!StringUtils.isEmpty(get)) {
                System.out.println(key);
                System.out.println(value);
                System.out.println("======");
            }

//            // 同时有get及post请求的
//            if (!StringUtils.isEmpty(post) && !StringUtils.isEmpty(get)) {
//                System.out.println(key);
//                System.out.println(value);
//                System.out.println("----");
//            }
        });
    }
}

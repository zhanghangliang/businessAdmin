package com.ob.common.businessAdmin.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.ob.common.businessAdmin.base.DataException;
import com.ob.common.businessAdmin.base.ResponseResult;
import com.ob.common.businessAdmin.config.PreGetSwaggerFactory;
import com.ob.common.businessAdmin.domain.CommonReqDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("businessAdmin/common")
public class BusinessAdminController {

    private String baseUrl = "http://server.dyzwdbk.com/gov-wiki-admin";

    private static final Logger LOG = Logger.getLogger(BusinessAdminController.class.getName());

    @PostMapping
    public void commonSend(@RequestBody(required = false) CommonReqDTO reqDTO, HttpServletResponse response) {
        LOG.info(JSONUtil.toJsonStr(reqDTO));
        ResponseResult<Object> result = new ResponseResult<>();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        String url = reqDTO.getUrl();
        Method method;
        Map<String, String> data = reqDTO.getData();
        try {
            if (PreGetSwaggerFactory.getMethodUrl.contains(url)) {
                method = Method.GET;
            } else if (PreGetSwaggerFactory.postMethodUrl.contains(url)) {
                method = Method.POST;
            } else {
                throw new DataException("500", String.format("请求消息体中的url[%s]不存在，请检查", url));
            }

            HttpRequest request = HttpUtil.createRequest(method, baseUrl + url).header("Content-Type", "application/json;charset=UTF-8");
            request.body(JSONUtil.toJsonStr(data));
            String res = request.execute().body();
            response.getWriter().write(res);
            result.setData(reqDTO);
        } catch (Exception e) {
            result.putException(e);
        }
    }
}

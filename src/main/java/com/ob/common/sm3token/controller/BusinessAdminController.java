package com.ob.common.sm3token.controller;

import cn.hutool.json.JSONUtil;
import com.ob.common.sm3token.base.ResponseResult;
import com.ob.common.sm3token.domain.CommonReqDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("businessAdmin/common")
public class BusinessAdminController {

    private static final Logger LOG = Logger.getLogger(BusinessAdminController.class.getName());

    @PostMapping
    public void commonSend(@RequestBody(required = false) CommonReqDTO reqDTO, HttpServletResponse response) {
        LOG.info(JSONUtil.toJsonStr(reqDTO));
        ResponseResult<Object> result = new ResponseResult<>();
        try {
            result.setData(reqDTO);
        } catch (Exception e) {
            result.putException(e);
        } finally {
            try {
                response.getWriter().write(JSONUtil.toJsonStr(result));
                response.setHeader("Content-Type", "application/json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

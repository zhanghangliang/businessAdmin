package com.ob.common.businessAdmin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.ob.common.businessAdmin.base.DataException;
import com.ob.common.businessAdmin.base.ResponseResult;
import com.ob.common.businessAdmin.config.PreGetSwaggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("businessAdmin/common")
public class BusinessAdminController {

    private static final String BASE_URL = "http://server.dyzwdbk.com/gov-wiki-admin";

    private static final Logger LOG = Logger.getLogger(BusinessAdminController.class.getName());

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void commonSend(@RequestParam MultipartFile[] files, @RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse resp) {
        ResponseResult<Object> result = new ResponseResult<>();
        resp.setCharacterEncoding("UTF-8");

        if(!StringUtils.isEmpty(req.getHeader("token")) && !params.containsKey("token")) {
            params.put("token", req.getHeader("token"));
        }

        String url = params.getOrDefault("url", "");
        int index = url.indexOf("?");
        String methodType = "未知" + url;
        String res = "未返回";
        String urlWithoutParam = url.substring(0, index == -1 ? url.length() : index);
        try {
            if (PreGetSwaggerFactory.UPLOAD_METHOD_URL.contains(urlWithoutParam)) {
                methodType = "上传链接";
                res = uploadMethod(files, result, Method.POST, params, resp);
            } else if (PreGetSwaggerFactory.DOWNLOAD_METHOD_URL.contains(urlWithoutParam)) {
                methodType = "下载链接";
                downloadMethod(result, params, resp);
            } else if (PreGetSwaggerFactory.GET_METHOD_URL.contains(urlWithoutParam)) {
                methodType = "普通get链接";
                res = simpleGetOrPost(result, Method.GET, params, resp);
            } else if (PreGetSwaggerFactory.POST_METHOD_URL.contains(urlWithoutParam)) {
                methodType = "普通post链接";
                res = simpleGetOrPost(result, Method.POST, params, resp);
            } else {
                throw new DataException("500", String.format("请求消息体中的url[%s]不存在,请检查", urlWithoutParam));
            }
        } catch (Exception e) {
            result.putException(e);
            try {
                resp.getWriter().write(JSONUtil.toJsonStr(result));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {
            LOG.info(String.format("[%s], 文件数量[%s], \n请求参数[%s], \n第三方返回值[%s]\n", methodType, files.length, JSONUtil.toJsonStr(params), res));
        }
    }

    /**
     * 处理普通的get、post请求
     */
    private String simpleGetOrPost(ResponseResult<Object> result, Method method, Map<String, String> params, HttpServletResponse resp) throws IOException {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + params.getOrDefault("url", ""))
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("token", params.getOrDefault("token", ""));
        request.body(params.getOrDefault("data", ""));
        String res = request.execute().body();
        result.setData(res);
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
//        resp.getWriter().write(JSONUtil.toJsonStr(result));
        resp.getWriter().write(res);
        return res;
    }

    /**
     * 处理上传请求
     */
    private String uploadMethod(MultipartFile[] files, ResponseResult<Object> result, Method method, Map<String, String> params, HttpServletResponse resp) throws IOException {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + params.getOrDefault("url", ""))
                .header("Content-Type", "multipart/form-data")
                .header("token", params.getOrDefault("token", ""));
        request.form("files", files);
        String res = request.execute().body();
        result.setData(res);
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
//        resp.getWriter().write(JSONUtil.toJsonStr(result));
        resp.getWriter().write(res);
        return res;
    }

    /**
     * 处理下载请求
     */
    private void downloadMethod(ResponseResult<Object> result, Map<String, String> params, HttpServletResponse resp) throws IOException {
        File tmp = File.createTempFile("baDownload", ".tmp");
        HttpRequest req = HttpUtil.createGet(BASE_URL + params.getOrDefault("url", ""))
                .header("token", params.getOrDefault("token", ""));
        req.executeAsync().writeBody(tmp);

        byte[] bytes = FileUtil.readBytes(tmp);
        // TODO 下载请求需要单独处理
    }
}

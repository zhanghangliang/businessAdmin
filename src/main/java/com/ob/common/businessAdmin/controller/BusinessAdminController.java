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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping
    public void commonSend(@RequestParam MultipartFile[] files, @RequestParam Map<String, String> params, HttpServletResponse resp) {
        LOG.info(String.format("文件数量[%s],请求参数[%s]", files.length, JSONUtil.toJsonStr(params)));
        ResponseResult<Object> result = new ResponseResult<>();
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");

        String url = params.getOrDefault("url", "");
        try {
            if (PreGetSwaggerFactory.UPLOAD_METHOD_URL.contains(url)) {
                // 上传链接
                uploadMethod(files, result, Method.POST, params, resp);
            } else if (PreGetSwaggerFactory.DOWNLOAD_METHOD_URL.contains(url)) {
                // 下载链接
                downloadMethod(result, params, resp);
            } else if (PreGetSwaggerFactory.GET_METHOD_URL.contains(url)) {
                // 普通get链接
                simpleGetOrPost(result, Method.GET, params, resp);
            } else if (PreGetSwaggerFactory.POST_METHOD_URL.contains(url)) {
                // 普通post链接
                simpleGetOrPost(result, Method.POST, params, resp);
            } else {
                throw new DataException("500", String.format("请求消息体中的url[%s]不存在，请检查", url));
            }
        } catch (Exception e) {
            result.putException(e);
        }
    }

    /**
     * 处理普通的get、post请求
     */
    private void simpleGetOrPost(ResponseResult<Object> result, Method method, Map<String, String> params, HttpServletResponse resp) throws IOException {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + params.getOrDefault("url", ""))
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("token", params.getOrDefault("token", ""));
        request.body(params.getOrDefault("data", ""));
        String res = request.execute().body();
        result.setData(res);
        resp.getWriter().write(JSONUtil.toJsonStr(result));
    }

    /**
     * 处理上传请求
     */
    private void uploadMethod(MultipartFile[] files, ResponseResult<Object> result, Method method, Map<String, String> params, HttpServletResponse resp) throws IOException {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + params.getOrDefault("url", ""))
                .header("Content-Type", "multipart/form-data");
        request.form("files", files);
        request.form("token", params.getOrDefault("token", ""));
        String res = request.execute().body();
        result.setData(res);
        resp.getWriter().write(JSONUtil.toJsonStr(result));
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

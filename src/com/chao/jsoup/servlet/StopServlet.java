package com.chao.jsoup.servlet;

import com.chao.jsoup.request.UpdateBaiSiBuDeJie;
import com.chao.jsoup.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 主页入口
 * Created by Chao on 2017/8/11.
 */
@WebServlet("/stop")
public class StopServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("POST")) {
            resp.setCharacterEncoding("UTF-8");
        } else {
            resp.setContentType("text/html;charset=utf-8");
        }
        UpdateBaiSiBuDeJie.getInstance().stop();
        resp.getWriter().write(JsonUtil.toJsonData(UpdateBaiSiBuDeJie.getInstance().isStartRun() ? "爬虫服务正在运行！" : "爬虫服务已经停止！"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

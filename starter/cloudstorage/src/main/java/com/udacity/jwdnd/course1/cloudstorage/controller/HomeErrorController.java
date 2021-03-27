package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeErrorController implements ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeErrorController.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model){
        String errorPage = "error";
        String pageTitle = "Error";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()){
                pageTitle = "404 - Page Not Found";
                errorPage = "error/404";
                LOGGER.error("Error 404");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                pageTitle = "500 - Internal Server Error";
                errorPage = "error/500";
                LOGGER.error("Error 500");
            } else if(statusCode == HttpStatus.FORBIDDEN.value()){
                pageTitle = "403 - Forbidden";
                errorPage = "error/403";
                LOGGER.error("Error 403");
            } else if(statusCode == HttpStatus.BAD_REQUEST.value()){
                pageTitle = "400 - Bad Request";
                errorPage = "error/400";
                LOGGER.error("Error 400");
            } else if(statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()){
                pageTitle = "405 - Method Not Allowed";
                errorPage = "error/405";
                LOGGER.error("Error 405");
            }
        }
        model.addAttribute("pageTitle",pageTitle);

        return errorPage;
    }
}

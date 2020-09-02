package com.zt.security.controller;


import com.alibaba.fastjson.JSON;
import com.zt.security.bo.MyUserDetails;
import com.zt.security.model.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@RestController
public class IndexController {

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private TokenUtil tokenUtil;

    //登录
//    @PostMapping("/login")
//    public Object login(@RequestBody MyUser user, HttpServletResponse response){
//        JSONObject jsonObject=new JSONObject();
//        MyUserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//        if(userDetails == null){
//            jsonObject.put("message","登录失败,用户不存在");
//            return jsonObject;
//        }
//        if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())){
//            jsonObject.put("message","登录失败,密码错误");
//            return jsonObject;
//        }
////        String token = tokenUtil.generateToken(userDetails.getMyUser());
////        jsonObject.put("token", token);
////        jsonObject.put("user", userDetails);
//        //jwt写入Authentication头
////        response.setHeader(Header);
//        return jsonObject;
//    }

    /**
     * 客户端通过这个端点用token获取用户信息
     * @param principal
     * @return
     */
    @GetMapping("/user/me")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }

    //登录
    @GetMapping("/token/get")
    @ResponseBody
    public Object token(HttpServletResponse response){
        //jwt写入Authentication头
        MyUser user = new MyUser();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setCreatedAt(new Date());
//        String s = tokenUtil.generateToken(user);
//        response.setHeader(Consts.HEADER_STRING, s);
//        return s;
        return null;
    }

    @RequestMapping("/")
    public String user(Model  model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        model.addAttribute("user", JSON.toJSONString(myUserDetails.getMyUser()));
        model.addAttribute("permissions", JSON.toJSONString(myUserDetails.getPermissions()));
        return "index";
    }

    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority('user:list')")
    public void userList(HttpServletResponse response) throws IOException {
        response.getWriter().write("permission user:list ");
    }

    @GetMapping("/user/update")
    @PreAuthorize("hasAuthority('user:update')")
    public void userUpdate(HttpServletResponse response) throws IOException {
        response.getWriter().write("permission user:update ");
    }

}

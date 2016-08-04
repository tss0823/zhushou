package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.dal.annotation.NotNeedLogin;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.enums.UserStatus;
import com.yuntao.zhushou.model.enums.UserType;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;


    @RequestMapping("register")
    @ResponseBody
    @NotNeedLogin
    public ResponseObject doRegister(@RequestParam String accountNo,
                                     @RequestParam String pwd) {
        // validate

        ResponseObject ret = new ResponseObject();
        User paramUser = new User();
        paramUser.setAccountNo(accountNo);
        paramUser.setPwd(pwd);
        paramUser.setType(UserType.dev.getCode());
        paramUser.setStatus(UserStatus.PASS.getCode());
//        User user = userService.register(paramUser);
//        ret.setData(user);
        return ret;
    }

    @RequestMapping("login")
    @ResponseBody
    public ResponseObject doLogin(@RequestParam String accountNo,
                                  @RequestParam String pwd) {
        // validate
        // login
        ResponseObject ret = new ResponseObject();
        User user = userService.login(accountNo, pwd);
        ret.setData(user);
        return ret;
    }

    @RequestMapping("logout")
    @ResponseBody
    @NeedLogin
    public ResponseObject logout() {
        User user = userService.getCurrentUser();
        userService.logout(user.getId());
        ResponseObject ret = new ResponseObject();
        return ret;
    }

    @RequestMapping("getLoginUser")
    @NeedLogin
    public ResponseObject getLoginUser() {
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        User user = userService.getCurrentUser();
        responseObject.setData(user);
        return responseObject;
    }

    @RequestMapping("list")
    @NeedLogin
    public String list(Integer pageSize, Integer pageNum, Model model) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNum == null) {
            pageNum = 1;
        }
//        PageInfo<User> pageInfo = userService.queryUserList(pageSize, pageNum);
//        model.addAttribute("pageInfo", pageInfo);
//        model.addAttribute("dataList", pageInfo.getRecords());
        return "user/userList";
    }

}

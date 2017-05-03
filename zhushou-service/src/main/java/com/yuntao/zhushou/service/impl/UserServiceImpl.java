package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.cache.CacheService;
import com.yuntao.zhushou.common.exception.BizException;
import com.yuntao.zhushou.common.utils.*;
import com.yuntao.zhushou.dal.mapper.UserMapper;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.UserQuery;
import com.yuntao.zhushou.model.vo.UserVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.service.inter.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByAccountNo(String accountNo) {
        UserQuery query = new UserQuery();
        query.setAccountNo(accountNo);
        Map<String,Object> queryMap = BeanUtils.beanToMap(query);
        User user = userMapper.findByCondition(queryMap);
        return user;
    }


    @Override
    public Pagination<UserVo> selectPage(UserQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = userMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<UserVo> pageInfo = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        List<User> dataList = userMapper.selectList(queryMap);
        List<UserVo> resultList = CollectUtils.transList(dataList,UserVo.class);
        pageInfo.setDataList(resultList);
        return pageInfo;
    }

    @Override
    public User login(String accountNo, String pwd) {
        User user = this.findByAccountNo(accountNo);
        if(user == null){
            throw new BizException("账号不存在");
        }
        if(!user.getPwd().equals(pwd)){
           throw new BizException("密码不正确");
        }
        setCurrentUser(user);
        return user;
    }

    @Override
    public void logout(Long userId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Cookie cookie = WebUtils.getCookie(request,"sid");
        if(cookie == null || StringUtils.isEmpty(cookie.getValue())){
            String sid = cookie.getValue();
            cacheService.remove("sid_"+sid);
            cacheService.remove("login_user_"+userId);
        }
    }

    @Override
    public User getCurrentUser() {
        //浏览器中cookie 缓存
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Cookie cookie = WebUtils.getCookie(request,"sid");
        if(cookie == null || StringUtils.isEmpty(cookie.getValue())){
            HttpServletResponse response = (HttpServletResponse) ResponseHolder.get();
            String uuid = UUID.randomUUID().toString();
            Cookie sidCookie = new Cookie("sid",uuid);
            sidCookie.setMaxAge(DateUtil.MONTH_SECONDS);
            sidCookie.setPath("/");
            response.addCookie(sidCookie);
            return null;
        }
        //获取userId
        String sid  = cookie.getValue();
        Object value = cacheService.get("sid_"+sid);
        if(value == null){
            return null;
        }
        Long userId = (Long) value;
        //从cache中获取
        User user = (User) cacheService.get("login_user_"+userId);
        if(user != null){
            return user;
        }
        user = this.findById(userId) ;
        if(user != null ){
            cacheService.set("login_user_"+userId,60 * 60 * 24 * 3,user);
        }
        return user;
    }

    @Override
    public void setCurrentUser(User user) {
        //浏览器中cookie 缓存
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Cookie cookie = WebUtils.getCookie(request,"sid");
        String sid = null;
        if(cookie == null || StringUtils.isEmpty(cookie.getValue())){
            HttpServletResponse response = (HttpServletResponse) ResponseHolder.get();
            sid = UUID.randomUUID().toString();
            Cookie sidCookie = new Cookie("sid",sid);
            sidCookie.setMaxAge(DateUtil.MONTH_SECONDS);
            sidCookie.setPath("/");
            response.addCookie(sidCookie);
        }else{
            sid = cookie.getValue();
        }
        //set cache userId
        cacheService.set("sid_"+sid,user.getId());

        //set cache user
        cacheService.set("login_user_"+user.getId(),user);

    }

    @Override
    public User getByEmail(String email) {
        UserQuery userQuery = new UserQuery();
        userQuery.setEmail(email);
        Map<String,Object> queryMap = BeanUtils.beanToMap(userQuery);
        User user = userMapper.findByCondition(queryMap);
        return user;
    }


}

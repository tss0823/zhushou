package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.UserQuery;
import com.yuntao.zhushou.model.vo.UserVo;
import com.yuntao.zhushou.common.web.Pagination;

public interface UserService {

    User findById(Long id);

    User findByAccountNo(String accountNo);

    Pagination<UserVo> selectPage(UserQuery query);

    User login(String accountNo, String pwd);

    void logout(Long userId);

    User getCurrentUser();

    void setCurrentUser(User user);


}

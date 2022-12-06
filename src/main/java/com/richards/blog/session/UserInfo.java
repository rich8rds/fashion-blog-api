package com.richards.blog.session;

import com.richards.blog.entity.User;
import com.richards.blog.exceptions.SessionIdNotFoundException;

import javax.servlet.http.HttpSession;

public class UserInfo {
    public static Long getUserSessionId(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("userDetails");
        if(user == null) return 0L;
        return user.getId();
    }

    public static Long userInSessionOrElseThrow(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("userDetails");
        if(user == null) throw new SessionIdNotFoundException("You have not mde a comment yet!");
        return user.getId();
    }

    public static User getUser(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("userDetails");
        if(user == null) throw new SessionIdNotFoundException("You are not logged in yet!");
        return user;
    }
}

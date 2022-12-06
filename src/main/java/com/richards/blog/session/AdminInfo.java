package com.richards.blog.session;

import com.richards.blog.entity.Admin;
import com.richards.blog.enums.Role;
import com.richards.blog.exceptions.UnAuthorizedException;

import javax.servlet.http.HttpSession;

public class AdminInfo {
    public static Long getAdminSessionId(HttpSession httpSession) {
        Admin admin = (Admin) httpSession.getAttribute("adminInfo");
        if (admin == null) throw new UnAuthorizedException("You do not have the right to access to this page.");
        return admin.getId();

    }

    public static Role getAdminRole(HttpSession httpSession) {
        Admin admin = (Admin) httpSession.getAttribute("adminInfo");
        if (admin == null) throw new UnAuthorizedException("You do not have the right to access to this page.");
        return admin.getRole();
    }
}

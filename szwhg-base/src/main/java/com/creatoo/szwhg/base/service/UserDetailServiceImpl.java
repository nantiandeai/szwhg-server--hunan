package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.model.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by yunyan on 2017/8/15.
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService{
    @Autowired
    private ManagerService managerService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Manager manager=managerService.getManagerByUsername(s);
        if(manager==null) throw new UsernameNotFoundException("用户不存在");
        if(manager.getIsLocked()!=null && manager.getIsLocked()==true) throw new LockedException("用户锁定");
        return Optional.ofNullable(manager).map(user -> {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList();
            return new User(user.getUsername(),user.getPassword(),grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("用户" + s + "不存在!"));
    }
}

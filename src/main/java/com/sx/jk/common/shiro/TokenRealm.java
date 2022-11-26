package com.sx.jk.common.shiro;

import com.sx.jk.common.cache.Caches;
import com.sx.jk.common.util.Streams;
import com.sx.jk.pojo.po.SysResource;
import com.sx.jk.pojo.po.SysRole;
import com.sx.jk.pojo.po.SysUser;
import com.sx.jk.service.SysResourceService;
import com.sx.jk.service.SysRoleService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class TokenRealm extends AuthorizingRealm {
  @Autowired
  private SysRoleService roleService;
  @Autowired
  private SysResourceService resourceService;

  public TokenRealm(CredentialsMatcher matcher) {
    super(matcher);
  }

  // 一进来就判断token是否为我们定义的Token类型
  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof Token;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    // 拿到当前登录用户的token
    String token = (String) principalCollection.getPrimaryPrincipal();

    // 根据token查找用户的角色、权限，在缓存中我们存的key为token，value为user
    SysUser user = Caches.getToken(token);

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();


    List<SysRole> roles = roleService.listByUserId(user.getId());
    if (CollectionUtils.isEmpty(roles)) return info;
    // 添加角色
    for (SysRole role : roles) {
      info.addRole(role.getName());
    }

    List<Short> roleIds = Streams.map(roles, SysRole::getId);
    List<SysResource> resources = resourceService.listByRoleIds(roleIds);
    if (CollectionUtils.isEmpty(resources)) return info;
    // 添加权限
    for (SysResource resource : resources) {
      info.addStringPermission(resource.getPermission());
    }

    return info;
  }

  // 61-2
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    String token = ((Token) authenticationToken).getToken();

    // SimpleAuthenticationInfo认证信息对象 也就是数据查到的info，这里我们把账号密码都设为token
    return new SimpleAuthenticationInfo(token, token, getName());
  }
}

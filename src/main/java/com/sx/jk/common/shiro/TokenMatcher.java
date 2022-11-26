package com.sx.jk.common.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

@Slf4j
public class TokenMatcher implements CredentialsMatcher {
  @Override
  public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
    log.debug("TokenMatcher - doCredentialsMatch - {} {}", authenticationToken, authenticationInfo);
    return true;
  }
}

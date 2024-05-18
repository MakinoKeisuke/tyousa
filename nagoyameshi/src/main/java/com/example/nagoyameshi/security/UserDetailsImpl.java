package com.example.nagoyameshi.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagoyameshi.entity.Member;

public class UserDetailsImpl implements UserDetails {
	private final Member member;
	private final Collection<GrantedAuthority> authorities;
	
	
	public UserDetailsImpl(Member member, Collection<GrantedAuthority> authorities) {
		this.member = member;
		this.authorities = authorities;
	}
	
	public Member getMember() {
		return member;
	}
	
	//ハッシュ化済みのパスワードを返す
	@Override
	public String getPassword() {
		// TODO 自動生成されたメソッド・スタブ
		return member.getPassword();
	}
	
	//ログイン時に利用するユーザー名(メールアドレス)を返す
	@Override
	public String getUsername() {
		return member.getEmail();
	}
	
	//ロールのコレクションを返す
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO 自動生成されたメソッド・スタブ
		return authorities;
	}
	
	//アカウントが期限切れでなければtrueを返す
	@Override
	public boolean isAccountNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}
	
	//ユーザーがロックされていなければtrueを返す
	@Override
	public boolean isAccountNonLocked() {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}
	
	//ユーザーのパスワードが期限切れでなければtrueを返す
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}
	
	//ユーザーが有効であればtrueを返す
	@Override
	public boolean isEnabled() {
		// TODO 自動生成されたメソッド・スタブ
		return member.getEnabled();
	}

}

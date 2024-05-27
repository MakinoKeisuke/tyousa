package com.example.nagoyameshi.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.repository.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final MemberRepository	 memberRepository;
	
	public UserDetailsServiceImpl(MemberRepository	memberRepository) {
		this.memberRepository = memberRepository;
	}
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			System.out.println("loadUserByUsername:" + email);

			Member member = memberRepository.findByEmail(email);
			if (member == null) {
				throw new UsernameNotFoundException("ユーザーが見つかりませんでした。(target member does not found.)");
			}
	    
			String memberRoleName = member.getRole().getName();
			System.out.println("memberRoleName" + memberRoleName);
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(memberRoleName));

		    return new UserDetailsImpl(member, authorities);
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}
	}
}

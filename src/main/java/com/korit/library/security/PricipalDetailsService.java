package com.korit.library.security;

import com.korit.library.aop.annotation.ParamsAspect;
import com.korit.library.repository.AccountRepository;
import com.korit.library.web.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PricipalDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @ParamsAspect
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB 에서 username 가져옴
        UserDto user = accountRepository.findUserByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("회원 정보를 확인 할 수 없음.");
        }
        
        //아이디를 찾으면 PricipalDetails 생성
        return new PricipalDetails(user);
    }
}

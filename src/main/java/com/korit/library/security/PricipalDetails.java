package com.korit.library.security;

import com.korit.library.web.dto.RoleDtlDto;
import com.korit.library.web.dto.RoleMstDto;
import com.korit.library.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@AllArgsConstructor
public class PricipalDetails implements UserDetails {

    //커스텀으로 생성
    @Getter
    private final UserDto user;
    private Map<String, Object> response;


    //권한을 리스트로 관리하는 부분
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        //람다식을 이해하는 반복문
        List<RoleDtlDto> roleDtlDtoList = user.getRoleDtlDto();
        for(int i=0;i< roleDtlDtoList.size();i++){
            RoleDtlDto dtl = roleDtlDtoList.get(i);
            RoleMstDto roleMstDto = dtl.getRoleMstDto();
            String roleName = roleMstDto.getRoleName();

            GrantedAuthority role = new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return roleName;
                }
            };
            authorities.add(role);
//            System.out.println(roleName == role.getAuthority());

        }

//        user.getRoleDtlDto().forEach(dtl -> {
//            authorities.add(() -> dtl.getRoleMstDto().getRoleName());
//        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword(); //DB user 의 해당 패스워드를 보고 복호화를함
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


    /*
        계정 만료 여부(기간이 지나면 못쓰게만듬)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
        계정 잠김 여부(블랙리스트)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
        비밀번호 만료 여부(비밀번호 5번 이상 틀렸을때)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
        사용자 활성화 여부(인증을 하지 않은 유저들 false)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

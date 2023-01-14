package com.korit.library.web.api;


import com.korit.library.aop.annotation.ValidAspect;
import com.korit.library.security.PricipalDetails;
import com.korit.library.service.AccountService;
import com.korit.library.web.dto.CMRespDto;
import com.korit.library.web.dto.UserDto;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@Api(tags = {"Account Rest API Controller"})
@RestController
@RequestMapping("/api/account")
public class AccountApi {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "회원가입", notes = "회원가입 요청 메소드")
    @ValidAspect
    @PostMapping("/register")
    public ResponseEntity<? extends CMRespDto<? extends UserDto>> register(@RequestBody @Valid UserDto userDto, BindingResult bindingResult){

        accountService.duplicateUsername(userDto.getUsername());
        accountService.compareToPassword(userDto.getPassword(),userDto.getRepassword());

        UserDto user = accountService.registerUser(userDto); //회원가입된 유저


        return ResponseEntity
                .created(URI.create("/api/account/user/" + user.getUserId()))
                .body(new CMRespDto<>(HttpStatus.CREATED.value(),"Create a new User",user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "사용자 식별 코드", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "클라이언트가 잘못했음"),
            @ApiResponse(code = 401, message = "클라이언트가 잘못했음")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<? extends CMRespDto<? extends UserDto>> getUser(
//            @ApiParam(value = "사용자 식별 코드")
            @PathVariable int userId){
        return ResponseEntity
                .ok()
                .body(new CMRespDto<>(HttpStatus.CREATED.OK.value(),"Success", accountService.getUser(userId)));
    }

    @ApiOperation(value = "Get Prin기cipal\", notes = \"로그인된 사용자 정보 가져오")
    @GetMapping("/principal")
    public ResponseEntity<CMRespDto<? extends PricipalDetails>> getPrincipalDetails(@ApiParam(hidden = true) @AuthenticationPrincipal PricipalDetails pricipalDetails){
        pricipalDetails.getAuthorities().forEach(role ->{
            log.info("로그인된 사용자의 권한: ", role.getAuthority());
        });

        return ResponseEntity
                .ok()
                .body(new CMRespDto<>(HttpStatus.OK.value(), "Success",pricipalDetails));
    }

}

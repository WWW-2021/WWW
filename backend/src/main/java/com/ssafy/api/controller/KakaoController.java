package com.ssafy.api.controller;

import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.service.KakaoAPI;
import com.ssafy.api.service.RedisService;
import com.ssafy.api.service.UserService;
import com.ssafy.common.Util.CookieUtil;
import com.ssafy.db.entity.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static java.util.Objects.isNull;


@CrossOrigin(origins={"*"}, maxAge=6000)
@RestController
@RequestMapping("/api/kakao")
public class KakaoController {
    @Autowired
    private KakaoAPI kakaoAPI;
    @Autowired
    UserService userService;
    @Autowired
    CookieUtil cookieUtil;

    @RequestMapping(value="/login")
    public ResponseEntity<UserLoginPostRes> login(@RequestParam("code") String code, HttpSession session) {
        HashMap<String,Object> Token = kakaoAPI.getAccessToken(code);
        // Token 정보를 <String, 객체>로 생성
        String accessToken = (String) Token.get("accessToken");
        String refreshToken = (String) Token.get("refreshToken");
        Long accessTokenExpire = Long.parseLong(Token.get("accessTokenExpire").toString());
        Long refreshTokenExpire = Long.parseLong(Token.get("refreshTokenExpire").toString());

        HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(accessToken);

        System.out.println("login Controller : " + userInfo);

        // 해당 회원이 존재하는지 확인
        String userId = (String) userInfo.get("userId");
        User user = userService.getUserId(userId);

        // 회원가입이 되어있는 경우
        if(!isNull(user)){
            // 쿠키로 할 경우
            //Cookie accessTokenCookie = cookieUtil.createCookie("accessToken",accessTokenExpire,accessToken);
            //Cookie refreshTokenCookie = cookieUtil.createCookie("refreshToken",refreshTokenExpire,refreshToken);
            session.setAttribute("userId", userInfo.get("userId"));
            session.setAttribute("access_Token", accessToken);
            return ResponseEntity.ok(UserLoginPostRes.of(200,"Success", userInfo));
        }
        // 없는 경우 유저 생성
        userService.createUser(Token,userInfo);

        return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", userInfo));
    }

    @ApiOperation(value = "카카오 계정 로그아웃", notes = "Kakao 인증서버에 accessToken을 넘겨주어 로그인된 카카오 계정을 로그아웃 시킨다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        String access_Token = (String) session.getAttribute("access_Token");
        if (access_Token == null){
            return ResponseEntity.ok("토큰이 유효하지 않습니다.");
        }
        kakaoAPI.Logout(access_Token);
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }


//    @ApiOperation(value = "카카오 계정 로그아웃", notes = "Kakao 인증서버에 accessToken을 넘겨주어 로그인된 카카오 계정을 로그아웃 시킨다.")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "성공"),
//            @ApiResponse(code = 401, message = "인증 실패"),
//            @ApiResponse(code = 404, message = "사용자 없음"),
//            @ApiResponse(code = 500, message = "서버 오류")
//    })
//    @GetMapping(value = "/logout")
//    public ResponseEntity<String> logout(@CookieValue(value = "accessToken", required = false) Cookie access_Token) {
//        if (access_Token == null)
//            return ResponseEntity.ok("토큰이 유효하지 않습니다.");
//        System.out.println("logout accessToken : " + access_Token.getValue());
//        kakaoAPI.Logout(access_Token.getValue());
//        return ResponseEntity.ok("로그아웃 되었습니다.");
//    }
}


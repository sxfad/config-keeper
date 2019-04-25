package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.dto.AuthUser;
import com.suixingpay.config.server.entity.MenuDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.YesNo;
import com.suixingpay.config.server.form.LoginForm;
import com.suixingpay.config.server.security.TokenHelper;
import com.suixingpay.config.server.security.TokenProperties;
import com.suixingpay.config.server.security.TokenWapper;
import com.suixingpay.config.server.security.repository.TokenRepository;
import com.suixingpay.config.server.service.UserService;
import com.suixingpay.config.server.util.KaptchaUtil;
import com.suixingpay.config.server.util.PasswordUtil;
import com.suixingpay.config.server.util.SecurityUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:09:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:09:33
 */
@Api(description = "鉴权接口")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final String LOGIN_FAIL_MSG = "您输入的用户名或密码不正确，请核对后再试！";

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository<AuthUser> tokenRepository;

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    public KaptchaUtil kaptchaUtil;

    @Value("${spring.application.name}")
    private String applicationName;

    @PostMapping({"/login"})
    public ResponseDTO<String> login(@RequestBody @Validated LoginForm form) {
        ResponseDTO<String> res = new ResponseDTO<>();
        if (!kaptchaUtil.check(form.getKey(), form.getCode())) {
            return res.addErrorMessage("验证码错误");
        }
        UserDO user = userService.getByName(form.getUsername());

        if (null == user) {
            return res.addErrorMessage(LOGIN_FAIL_MSG);
        }
        if (!user.getPassword().equals(PasswordUtil.getMd5Password(form.getPassword()))) {
            return res.addErrorMessage(LOGIN_FAIL_MSG);
        }
        String[] roles = new String[]{
                user.getAdministrator() == YesNo.YES ? UserService.HAS_SUPER_ROLE : UserService.HAS_NORMAL_ROLE};
        AuthUser authUser = new AuthUser(user, roles);
        String token = TokenHelper.genToken(applicationName, user.getId());
        tokenRepository.set(token, authUser, tokenProperties.getTimeout());
        res.setData(token);
        return res;
    }

    @PostMapping("/logout")
    public ResponseDTO<Void> logout(HttpServletRequest request) {
        String token = null;
        Optional<TokenWapper> info = TokenHelper.getTokenWapper(request);
        if (info.isPresent()) {
            TokenWapper tokenWapper = info.get();
            token = tokenWapper.getToken();
        }
        if (null != token && token.length() > 0) {
            tokenRepository.del(token);
        }
        return new ResponseDTO<Void>();
    }

    @GetMapping("/my")
    public ResponseDTO<UserDO> my(HttpServletRequest request) {
        UserDO userDO = SecurityUtil.getSessionUser(request).get();
        userDO.setPassword(null);
        userDO.setEmail(null);
        return new ResponseDTO<UserDO>(userDO);
    }

    @GetMapping("/getMenu")
    public ResponseDTO<List<MenuDO>> getMenu(HttpServletRequest request) {
        UserDO userDO = SecurityUtil.getSessionUser(request).get();
        List<MenuDO> menuDOList = new ArrayList<>();
        if (userDO.getAdministrator() == YesNo.YES) {
            menuDOList.add(MenuDO.builder().key("0").icon("fa-support").path("/").text("随行付统一配置中心").build());

            menuDOList.add(MenuDO.builder().key("home").parentKey("0").icon("fa-home").path("/").text("首页").build());

            menuDOList.add(MenuDO.builder().key("1").parentKey("0").icon("fa-user").path("/base-information/users")
                    .text("用户管理").build());
            menuDOList.add(MenuDO.builder().key("2").parentKey("0").icon("fa-cloud").path("/base-information/profile")
                    .text("环境管理").build());
            menuDOList.add(MenuDO.builder().key("3").parentKey("0").icon("fa-sitemap")
                    .path("/base-information/application").text("应用管理").build());
            menuDOList.add(MenuDO.builder().key("4").parentKey("0").icon("fa-cog")
                    .path("/base-information/application-config").text("应用配置").build());
            menuDOList.add(MenuDO.builder().key("5").parentKey("0").icon("coffee")
                    .path("/base-information/global-config").text("全局配置").build());
        } else {
            menuDOList.add(MenuDO.builder().key("0").icon("fa-support").path("/").text("随行付统一配置中心").build());

            menuDOList.add(MenuDO.builder().key("home").parentKey("0").icon("fa-home").path("/").text("首页").build());

            menuDOList.add(MenuDO.builder().key("4").parentKey("0").icon("fa-cog")
                    .path("/base-information/application-config").text("应用配置").build());
            menuDOList.add(MenuDO.builder().key("5").parentKey("0").icon("coffee")
                    .path("/base-information/global-config").text("全局配置").build());
        }
        return new ResponseDTO<List<MenuDO>>(menuDOList);
    }

}

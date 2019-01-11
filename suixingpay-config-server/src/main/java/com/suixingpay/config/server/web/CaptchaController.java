package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.dto.Kaptcha;
import com.suixingpay.config.server.util.KaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 图片验证码
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午4:52:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午4:52:42
 */
@Api(description = "图片验证码接口")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    public KaptchaUtil kaptchaUtil;

    /**
     * 生成验证码
     *
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "生成验证码", notes = "生成验证码")
    @GetMapping("/gen")
    public ResponseDTO<Kaptcha> getCaptchaCode(HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        return new ResponseDTO<Kaptcha>(kaptchaUtil.createImage());
    }

    /**
     * 检查验证码是否正确
     *
     * @param key
     * @param captchaCode
     * @return
     */
    @ApiOperation(value = "检查验证码是否正确", notes = "检查验证码是否正确")
    @GetMapping("/check")
    public ResponseDTO<Boolean> checkCaptchaCode(@ApiParam(name = "key", value = "验证码key", required = true) //
                                                 @Valid @NotNull(message = "验证码key不能为空") //
                                                 @RequestParam("key") String key, //
                                                 @ApiParam(name = "code", value = "验证码", required = true) //
                                                 @Valid @NotBlank(message = "验证码不能为空") //
                                                 @RequestParam("code") String code) {
        return new ResponseDTO<>(kaptchaUtil.check(key, code));
    }
}

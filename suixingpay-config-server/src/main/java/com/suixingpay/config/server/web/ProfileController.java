package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.form.AddEditProfileForm;
import com.suixingpay.config.server.security.PreAuthorize;
import com.suixingpay.config.server.service.ProfileService;
import com.suixingpay.config.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 注：只有超级管理员才有权限管理Profile
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 上午9:43:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 上午9:43:52
 */
@Api(description = "Profile")
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "Profile列表", notes = "Profile列表")
    public ResponseDTO<List<ProfileDO>> list() {
        List<ProfileDO> list = this.profileService.listAll();
        return new ResponseDTO<List<ProfileDO>>(list);
    }

    @GetMapping("/{profile}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "Profile详情", notes = "Profile详情")
    public ResponseDTO<ProfileDO> detail(//
                                         @ApiParam(name = "profile", value = "profile", required = true) //
                                         @PathVariable("profile") @Valid @NotBlank(message = "profile不能为空") String profile) {
        ProfileDO profileDO = this.profileService.getByProfile(profile);
        return new ResponseDTO<ProfileDO>(profileDO);
    }

    @PostMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "添加Profile", notes = "添加Profile")
    public ResponseDTO<Void> addProfile(@RequestBody @Validated AddEditProfileForm addEditProfileForm) throws Exception {
        ProfileDO profileDO = addEditProfileForm.convertToProfileDO();
        profileService.addProfile(profileDO);
        return new ResponseDTO<Void>();
    }

    @PutMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "修改Profile", notes = "修改Profile")
    public ResponseDTO<Void> updateProfile(@RequestBody @Validated AddEditProfileForm addEditProfileForm) throws Exception {
        ProfileDO profileDO = addEditProfileForm.convertToProfileDO();
        profileService.updateProfile(profileDO);
        return new ResponseDTO<Void>();
    }

    /**
     * 检查主键是否重复
     *
     * @param profile
     * @return
     */
    @GetMapping("/checkKeyUnique/{profile}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "检查主键是否重复", notes = "检查主键是否重复")
    public ResponseDTO<Boolean> checkKeyUnique(@PathVariable("profile") String profile) {
        if (null == profileService.getByProfile(profile)) {
            // 不重复 可用
            return new ResponseDTO<Boolean>(false);
        }
        // 重复 不可用
        return new ResponseDTO<Boolean>(true);
    }
}

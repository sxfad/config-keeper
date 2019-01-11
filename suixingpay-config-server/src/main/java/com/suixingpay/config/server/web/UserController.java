package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.condition.UserCondition;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.Status;
import com.suixingpay.config.server.form.AddUserForm;
import com.suixingpay.config.server.form.UpdateUserForm;
import com.suixingpay.config.server.form.UserQueryForm;
import com.suixingpay.config.server.security.PreAuthorize;
import com.suixingpay.config.server.service.UserApplicationConfigRoleService;
import com.suixingpay.config.server.service.UserGlobalConfigRoleService;
import com.suixingpay.config.server.service.UserService;
import com.suixingpay.config.server.util.PasswordUtil;
import com.suixingpay.config.server.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * 只有超级管理员才有权限
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 上午9:54:21
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 上午9:54:21
 */
@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserApplicationConfigRoleService userApplicationConfigRoleService;

    @Autowired
    private UserGlobalConfigRoleService userGlobalConfigRoleService;

    @GetMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "用户列表", notes = "用户列表")
    public ResponseDTO<Page<UserDO>> list(UserQueryForm form) {
        UserCondition condition = form.convertToUserCondition();
        Page<UserDO> pageRes = this.userService.pageByCondition(condition);
        return new ResponseDTO<Page<UserDO>>(pageRes);
    }

    @GetMapping("/{id}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "详情", notes = "详情")
    public ResponseDTO<UserDO> detail(@ApiParam(name = "id", value = "ID", required = true) //
                                      @PathVariable("id") @Valid @NotNull(message = "ID不能为空") Integer id) {
        UserDO user = this.userService.getById(id);
        return new ResponseDTO<UserDO>(user);
    }

    @PostMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "添加用户", notes = "添加用户")
    public ResponseDTO<Void> addUser(@RequestBody @Valid AddUserForm form) throws Exception {
        UserDO user = form.convertToUserDO();
        userService.addUser(user);
        Integer userId = null == userService.getByName(user.getName()) ? null
                : userService.getByName(user.getName()).getId();
        // // 更新全局配置权限
        userGlobalConfigRoleService.saveRoles(userId, form.getProfileRoles());
        // // 更新应用环境权限
        userApplicationConfigRoleService.saveRoles(userId, form.getApplicationProfileRoles());
        return new ResponseDTO<Void>();
    }

    @PutMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "修改用户", notes = "修改用户")
    public ResponseDTO<Void> updateUser(@RequestBody @Valid UpdateUserForm form) throws Exception {
        UserDO user = form.convertToUserDO();
        userService.updateUser(user);
        // 更新全局配置权限
        userGlobalConfigRoleService.saveRoles(user.getId(), form.getProfileRoles());
        // 更新应用环境权限
        userApplicationConfigRoleService.saveRoles(user.getId(), form.getApplicationProfileRoles());

        return new ResponseDTO<Void>();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "注销", notes = "注销")
    public ResponseDTO<Void> cancel(//
                                    @ApiParam(name = "id", value = "ID", required = true) //
                                    @PathVariable("id") @Valid @NotNull(message = "ID不能为空") Integer id) throws Exception {
        UserDO user = new UserDO();
        user.setStatus(Status.INVALID);
        userService.updateUser(user);
        return new ResponseDTO<Void>();
    }

    @PatchMapping("/{id}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "激活", notes = "激活")
    public ResponseDTO<Void> activate(//
                                      @ApiParam(name = "id", value = "ID", required = true) //
                                      @PathVariable("id") @Valid @NotNull(message = "ID不能为空") Integer id) throws Exception {
        UserDO user = new UserDO();
        user.setStatus(Status.VALID);
        userService.updateUser(user);
        return new ResponseDTO<Void>();
    }

    @PutMapping("/{id}/reset_password")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public ResponseDTO<Void> resetPassword(//
                                           @ApiParam(name = "id", value = "ID", required = true) //
                                           @PathVariable("id") @Valid @NotNull(message = "ID不能为空") Integer id, //
                                           @ApiParam(value = "密码", required = true) @RequestParam(required = true) //
                                           @Valid @NotBlank(message = "请输入密码!") //
                                           @Length(min = 6, max = 40, message = "密码必须大于{min}位，且不能超过{max}位") String password) throws Exception {
        UserDO tmpUser = new UserDO();
        tmpUser.setId(id);
        tmpUser.setPassword(PasswordUtil.getMd5Password(password));
        userService.updateUser(tmpUser);
        return new ResponseDTO<Void>();
    }

    @PutMapping(value = "/change_self_password")
    @ApiOperation(value = "修改自己密码", notes = "修改自己密码")
    public ResponseDTO<Void> changeSelfPassword(HttpServletRequest request, //
                                                @ApiParam(value = "旧密码", required = true) @RequestParam(required = true)
                                                //
                                                @Valid @NotBlank(message = "请输入旧密码!") //
                                                @Length(min = 6, max = 40, message = "旧密码必须大于{min}位，且不能超过{max}位") String old_password, //
                                                @ApiParam(value = "新密码", required = true) @RequestParam(required = true)
                                                //
                                                @Valid @NotBlank(message = "请输入新密码!") //
                                                @Length(min = 6, max = 40, message = "新密码必须大于{min}位，且不能超过{max}位") //
                                                        String new_password) throws Exception {
        ResponseDTO<Void> res = new ResponseDTO<>();
        Optional<UserDO> userOpt = SecurityUtil.getSessionUser(request);
        if (!userOpt.isPresent()) {
            return res.addErrorMessage("请登录后重试！");
        }
        UserDO user = userService.getById(userOpt.get().getId());
        if (null == user) {
            return res.addErrorMessage("用户不存在！");
        }
        if (!user.getPassword().equals(PasswordUtil.getMd5Password(old_password))) {
            return res.addErrorMessage("旧密码不正确！");
        }

        UserDO tmpUser = new UserDO();
        tmpUser.setId(user.getId());
        tmpUser.setPassword(PasswordUtil.getMd5Password(new_password));
        userService.updateUser(tmpUser);
        return res;
    }

    @GetMapping(value = "/checkNameUnique/{name}")
    @ApiOperation(value = "检查登录名称是否唯一", notes = "检查登录名称是否唯一")
    public ResponseDTO<Boolean> checkNameUnique(@PathVariable("name") String name) {
        if (null != userService.getByName(name)) {
            return new ResponseDTO<Boolean>(true);
        }
        return new ResponseDTO<Boolean>(false);
    }

}

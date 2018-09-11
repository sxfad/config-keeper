const tipMessage = {
    loginNameIsRequired: '请输入登录名',
    passIsRequired: '请输入密码',
    passFormatError: '密码长度不能小于6位',
    newPassIsRequired: '请输入新密码',
    reNewPassIsRequired: '请输入确认新密码',
    towPassIsDifferent: '两次输入密码不一致',
    loginNameFormatError: '登录名不可用！',
    logoutError: '登录失败！',
    emailFormatError: '请输入正确的邮箱！',
    mobileFormatError: '请输入正确的电话号码！',
    canNotBeNull(name = '') {
        return `${name}不能为空！`;
    },
};
export default tipMessage;


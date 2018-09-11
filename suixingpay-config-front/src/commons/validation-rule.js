import {promiseAjax} from 'sx-ui';
import TipMessage from './tip-message';

export default {
    passWord(message) { // 密码验证
        return {
            pattern: /^[\s\S]{6,40}$/,
            message,
        };
    },
    checkLength(message) {
        return {
            pattern: /^[\s\S]{1,40}$/,
            message,
        };
    },
    mobile(message) { // 手机号
        return {
            pattern: /^1[3|4|5|7|8][0-9]{9}$/,
            message,
        };
    },
    landline(message) { // 座机
        return {
            pattern: /^([0-9]{3,4}-)?[0-9]{7,8}$/,
            message,
        };
    },
    qq(message) { // qq号
        return {
            pattern: /^[1-9][0-9]{4,9}$/,
            message,
        };
    },
    idCardNo(message) { // 身份证号十五位十八位最后X的校验
        return {
            pattern: /(^\d{15}$)|(^\d{17}([0-9]|X)$)/,
            message,
        };
    },
    number(message) { // 正整数
        return {
            pattern: /^[0-9]+$/,
            message,
        };
    },
    integer(message) { // 整数
        return {
            pattern: /^[-]{0,1}[0-9]{1,}$/,
            message,
        };
    },
    email(message) {
        return {type: 'email', message: message || TipMessage.emailFormatError};
    },
    required(name) {
        return {required: true, message: TipMessage.canNotBeNull(name)};
    },
    /**
     * 判断管理员登录名是否重复
     * @param ignoreValues {Array} 这些名字不进行检测，用于修改的情况。
     * @returns {*}
     */
    checkAdminLoginName(ignoreValues = []) {
        if (typeof ignoreValues === 'string') {
            ignoreValues = [ignoreValues];
        }
        return {
            validator(rule, value, callback) {
                if (!value || ignoreValues.indexOf(value) > -1) {
                    return callback();
                }
                promiseAjax
                    .get('/admins/checkLoginName', {loginName: value}, {errorTip: false})
                    .then(res => {
                        if (res) {
                            return callback([new Error('抱歉，该登录名已被占用！')]);
                        }
                        callback();
                    })
                    .catch(err => {
                        return callback([new Error((err && err.body && err.body.message) || '未知系统错误')]);
                    });
            },
        };
    },

    /**
     * 两个字段必填一个
     * @param field 另一个字段
     * @param form
     * @param errorTip
     * @returns {*}
     */
    twoFieldRelateRequired(field, form, errorTip) {
        const {getFieldValue, getFieldError, setFieldsValue} = form;
        return {
            validator(rule, value, callback) {
                const fieldValue = getFieldValue(field);
                if (!value && !fieldValue) {
                    return callback([new Error(errorTip)]);
                }
                if (value && !fieldValue && getFieldError(field)) {
                    setFieldsValue({[field]: ''});
                }
                callback();
            },
        };
    },

};

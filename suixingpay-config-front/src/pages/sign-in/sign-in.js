import {promiseAjax} from 'sx-ui';
import {convertToTree, getTopNodeByNode} from 'sx-ui/utils/tree-utils';
import './sign-in.css';
import {
    setCurrentLoginUser,
    getAjaxBaseUrl,
    setMenuData,
    setMenuTreeData
} from '../../commons/index';
import {init as initStorage, session} from 'sx-ui/utils/storage';

let isClickLogin = false;
let isFirstLogin = false;
const loginButton = document.getElementById('login-btn');
const nameEle = document.getElementById('name');
const passEle = document.getElementById('pass');
const codeEle = document.getElementById('code');

promiseAjax.init({
    setOptions: (instance) => {
    instance.defaults.baseURL = getAjaxBaseUrl();
},
});

function showError(error = '未知系统错误') {
    if (isFirstLogin) {
        const resetPassErrorEle = document.getElementById('reset-pass-error');
        resetPassErrorEle.innerHTML = error;
        resetPassErrorEle.innerText = error;// ie?
    } else {
        const loginErrorEle = document.getElementById('login-error');
        loginErrorEle.innerHTML = error;
        loginErrorEle.innerText = error;// ie?
    }
}

function clearError() {
    const loginErrorEle = document.getElementById('login-error');
    loginErrorEle.innerHTML = '';
    loginErrorEle.innerText = '';// ie?
}

function getcode() {
    promiseAjax.get('captcha/gen').then(res => {
        if (res.status === 'SUCCESS') {
        $('#codeimg').attr('src', res.data.base64Code);
        $('#codeimg').data('key', res.data.key);
    } else {
        showError(res.error.ERROR_MSG);
    }
})
}

// 点击获取新的验证码
$('.code-info').on('click', function () {
    getcode();
});

// 验证验证码
function validateCode(key, code) {
    let params = {
        key: key,
        code: code,
    };
    promiseAjax.get('captcha/check', params).then(res => {
        console.log(res);
    if (res.status === 'SUCCESS' && res.data) {
        console.log('验证成功');
    } else {
        console.log('验证失败');
    }
})
}

function handleLogin() {

    clearError();
    const name = nameEle.value;
    const pass = passEle.value;
    const code = codeEle.value;
    const key = $('#codeimg').data('key');
    if (!name) {
        showError('请输入用户名');
        return;
    }
    if (!pass) {
        showError('请输入密码');
        return;
    }
    let params = {
        username: name,
        password: pass,
        key: key,
        code: code,
    };
    promiseAjax.post('auth/login', params).then(rsp => {
        if (rsp.status === 'SUCCESS') {
        promiseAjax.init({
            setOptions: (instance) => {
            instance.defaults.baseURL = getAjaxBaseUrl();
        instance.defaults.headers = {
            'X-Token': rsp.data,
        };
    },
    });
        promiseAjax.get('auth/my').then(userInfo => {
            const currentLoginUser = {
                authToken: rsp.data,
                id: userInfo.data.id,
                name: userInfo.data.name,
                loginName: userInfo.data.name,
                administrator: userInfo.data.administrator,
            };
        initStorage({
            keyPrefix: currentLoginUser.id,
        });
        session.setItem('authToken', rsp.data);
        setCurrentLoginUser(currentLoginUser);

        promiseAjax.get('auth/getMenu').then(rsp => {
            isClickLogin = false;
        setMenuData(rsp.data);
        const menuTreeData = convertToTree(rsp.data.filter(item => item.path));
        setMenuTreeData(menuTreeData);
        getTopNodeByNode(menuTreeData, rsp.data[0]);
        window.location.href = '/';
    })
    });
    } else if (rsp.status === 'ERROR') {
        showError(rsp.error.ERROR_MSG);
    }
}).catch(err => {
        showError('系统错误，请重新登录');
});
}


function addHandler(element, type, handler) {
    if (!element) return;
    if (element.addEventListener) {
        element.addEventListener(type, handler, false);
    } else if (element.attachEvent) {
        element.attachEvent(`on${type}`, handler);
    } else {
        element[`on${type}`] = handler;
    }
}

addHandler(loginButton, 'click', handleLogin);
getcode();

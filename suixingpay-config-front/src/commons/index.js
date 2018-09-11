import {promiseAjax, isDev, isPro, isTest, isRC} from 'sx-ui';
import {session, setItem, getItem, removeItem} from 'sx-ui/utils/storage';
import devAjaxBaseUrl from '../../local/local-ajax-base-url';

export function isInternalSystem() {
    const runEnv = process.run_env.RUN_ENV;
    return runEnv === 'internalSystem' || runEnv === 'undefined' || runEnv === undefined;
}

export function isExternalSystem() {
    return process.run_env.RUN_ENV === 'externalSystem';
}

export function getAjaxBaseUrl() {
    // if (isDev) {
    //     return devAjaxBaseUrl;
    // }
    // if (isPro) {
    //     return '/api/';
    // }
    //
    // if (isTest) {
    //     return '/api/';
    // }
    //
    // if (isRC) {
    //     return '/api/';
    // }
    return '/';
}

export function getCurrentLoginUser() {
    const currentLoginUser = window.sessionStorage.getItem('currentLoginUser');
    return currentLoginUser ? JSON.parse(currentLoginUser) : null;
}


export function setCurrentLoginUser(user) {
    window.sessionStorage.setItem('currentLoginUser', JSON.stringify(user));
}

export function setUserType(userType) {
    setItem('administrator', userType);
}

export function getUserType() {
    return getItem('administrator');
}

export function toLogin() {
    location.href = '/signin.html';
    return false;
}

export function logout() {
    promiseAjax.post('/auth/logout', {}, {errorTip: false}).then(() => {
        session.clear();
        removeItem('menuData');
        removeItem('menuTreeData');
        window.sessionStorage.removeItem('currentLoginUser');
        toLogin();
    })
}

export function getMenuData() {
    return getItem('menuData');
}

export function setMenuData(menuTreeData) {
    setItem('menuData', menuTreeData);
}

export function getMenuTreeData() {
    return getItem('menuTreeData');
}

export function setMenuTreeData(menuTreeData) {
    setItem('menuTreeData', menuTreeData);
}


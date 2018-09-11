import {message} from 'antd';
import {logout} from './index';

export default function (error) {
    let msg = '操作失败';
    console.log(error);
    if (error.response) {
        const resData = error.response.data;
        const {status} = error.response;
        if (resData && resData.message) {
            msg = resData.message;
        }
        if (status === 401) {
            logout();
        }
        if (status === 404) {
            msg = '您访问的资源不存在！';
        }
        if (status === 403) {
            msg = '您无权访问此资源！';
        }
        if (resData && resData.message && resData.message.startsWith('timeout of')) {
            msg = '请求超时！';
        }
    }
    message.error(msg, 3);
}

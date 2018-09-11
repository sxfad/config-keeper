import {message} from 'antd';

export default function (rsp) {
    let msg = '操作失败';
    if (rsp.data) {
        // const resData = error.response.data;
        const status = rsp.data.status;
        if (status === 'ERROR') {
            let errorMsg = '';
            for (var e in rsp.data.error) {
                errorMsg += rsp.data.error[e] + "\n";
            }
            if (errorMsg !== '') {
                message.error(`${errorMsg}`, 5);
            } else {
                message.error(`${msg}`, 5);
            }
        }
    }
}

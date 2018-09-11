import * as PubSubMsg from 'sx-ui/utils/pubsubmsg';

export default function utilsMiddleware() {
    return next => action => {
        const {payload, error, meta = {}} = action;
        const {sequence = {}, successTip, errorTip = '未知系统错误'} = meta;
        // error handle
        if (errorTip && error) {
            console.error(payload);
            PubSubMsg.publish('message', {type: 'error', message: errorTip, error: payload});
        }
        if (sequence.type === 'next' && !error && successTip) {
            PubSubMsg.publish('message', {type: 'success', message: successTip});
        }
        next(action);
    };
}

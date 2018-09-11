export default function asyncActionCallbackMiddleware() {
    return next => action => {
        const {meta = {}, error, payload} = action;
        const {sequence = {}, resolved, rejected} = meta;
        if (sequence.type !== 'next') return next(action);

        // do callback
        if (error) {
            if (rejected) {
                rejected(payload);
            }
        } else if (resolved) {
            resolved(payload);
        }

        next(action);
    };
}

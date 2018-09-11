export default function handleAsyncReducer({
    always = (state) => state,
    pending = (state) => state,
    resolve = (state) => state,
    reject = (state) => state,
    complete = (state) => state,
}) {
    return (state, action) => {
        const {meta = {}, error} = action;
        const {sequence = {}} = meta;

        function getReturnState(method) {
            const newState = method(state, action);
            if (!newState) {
                throw Error(`handleAsyncReducer's ${method} method must return a new state`);
            }
            return newState;
        }

        const alwaysState = getReturnState(always);

        if (sequence.type === 'start') {
            const pendingState = getReturnState(pending);

            return {...alwaysState, ...pendingState};
        }

        if (sequence.type === 'next' && error) {
            const rejectState = getReturnState(reject);
            const completeState = getReturnState(complete);

            return {...alwaysState, ...rejectState, ...completeState};
        }

        if (sequence.type === 'next' && !error) {
            const resolveState = getReturnState(resolve);
            const completeState = getReturnState(complete);

            return {...alwaysState, ...resolveState, completeState};
        }
    };
}

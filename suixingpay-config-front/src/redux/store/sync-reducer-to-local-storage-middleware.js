import {isFSA} from 'flux-standard-action';
import * as storage from 'sx-ui/utils/storage';
import * as types from '../actionTypes';

export default ({dispatch, getState}) => next => action => {
    setTimeout(() => { // 使getState获取到更新后的state
        let pageState = getState().pageState;
        if (pageState) {
            Object.keys(pageState).forEach(key => {
                const state = pageState[key];
                if (state && state.sync === true) {
                    storage.setItem(key, state);
                }
            });
        }
    });

    if (!isFSA(action)) {
        return next(action);
    }
    const {meta = {}, sequence = {}, error, payload} = action;
    const {sync} = meta;

    if (action.type === types.SYNC_STATE_TO_STORAGE) {
        let state = getState();
        try {
            storage.setItem(payload, state[payload]);
        } catch (err) {
            /* eslint-disable */
            console.warn(err);
        }
    }

    if (!sync || sequence.type === 'start' || error) {
        return next(action);
    }

    next(action);

    setTimeout(() => {
        dispatch({
            type: types.SYNC_STATE_TO_STORAGE,
            payload: sync,
        });
    }, 16);
};

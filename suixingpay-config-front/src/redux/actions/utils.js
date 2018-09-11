import {createAction} from 'redux-actions';
import * as Storage from 'sx-ui/utils/storage';
import * as types from '../actionTypes';
import pageInitState from '../../page-init-state';

// 同步本地数据到state中
export const getStateFromStorage = createAction(types.GET_STATE_FROM_STORAGE, () => {
    let keys = ['setting'];
    if (pageInitState) {
        keys = keys.concat(Object.keys(pageInitState).map(key => {
            if (pageInitState[key] && pageInitState[key].sync === true) {
                return key;
            }
            return null;
        }));
    }
    return Storage.multiGet(keys);
}, (resolved, rejected) => {
    return {
        resolved,
        rejected,
    };
});

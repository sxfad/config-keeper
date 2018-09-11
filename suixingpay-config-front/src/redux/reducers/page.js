import {handleActions} from 'redux-actions';
import {cloneDeep} from 'lodash/lang';
import * as utils from 'sx-ui/utils';
import * as types from '../actionTypes';
import initialState from '../../page-init-state';


export default handleActions({
    [types.SET_STATE](state, action) {
        const {pageScope, payload} = action;
        // TODO state 是否需要深拷贝？
        const newState = cloneDeep(state);
        const newPayload = cloneDeep(payload);
        return {
            ...newState,
            [pageScope]: {...(newState[pageScope]), ...newPayload},
        };
    },
    [types.ARR_APPEND_VALUE](state, action) {
        const {pageScope, keyPath, value} = action;
        const newState = cloneDeep(state);
        const pageScopeState = newState[pageScope];
        utils.arrAppendValue(pageScopeState, keyPath, value);
        return {
            ...newState,
            [pageScope]: pageScopeState,
        };
    },
    [types.ARR_REMOVE_VALUE](state, action) {
        const {pageScope, keyPath, value} = action;
        const newState = cloneDeep(state);
        const pageScopeState = newState[pageScope];
        utils.arrRemove(pageScopeState, keyPath, value);
        return {
            ...newState,
            [pageScope]: pageScopeState,
        };
    },
    [types.ARR_REMOVE_ALL_VALUE](state, action) {
        const {pageScope, keyPath, value} = action;
        const newState = cloneDeep(state);
        const pageScopeState = newState[pageScope];
        utils.arrRemoveAll(pageScopeState, keyPath, value);
        return {
            ...newState,
            [pageScope]: pageScopeState,
        };
    },
    [types.OBJ_SET_VALUE](state, action) {
        const {pageScope, keyPath, value} = action;
        const newState = cloneDeep(state);
        const pageScopeState = newState[pageScope];
        utils.objSetValue(pageScopeState, keyPath, value);
        return {
            ...newState,
            [pageScope]: pageScopeState,
        };
    },
    [types.OBJ_REMOVE_VALUE](state, action) {
        const {pageScope, keyPath} = action;
        const newState = cloneDeep(state);
        const pageScopeState = newState[pageScope];
        utils.objRemove(pageScopeState, keyPath);
        return {
            ...newState,
            [pageScope]: pageScopeState,
        };
    },
    [types.GET_STATE_FROM_STORAGE](state, action) {
        const {payload} = action;
        const newState = cloneDeep(state);
        if (payload) {
            Object.keys(payload).forEach(key => {
                if (initialState && initialState[key] && payload[key]) {
                    newState[key] = payload[key];
                }
            });
        }
        return {
            ...state,
            ...newState,
        };
    },
}, initialState);

import * as types from '../actionTypes';

export function setScopeState(pageScope, payload) {
    return {
        type: types.SET_STATE,
        pageScope,
        payload,
    };
}

export function arrAppendValue(pageScope, keyPath, value) {
    return {
        type: types.ARR_APPEND_VALUE,
        pageScope,
        keyPath,
        value,
    };
}

export function arrRemoveValue(pageScope, keyPath, value) {
    return {
        type: types.ARR_REMOVE_VALUE,
        pageScope,
        keyPath,
        value,
    };
}


export function arrRemoveAllValue(pageScope, keyPath, value) {
    return {
        type: types.ARR_REMOVE_ALL_VALUE,
        pageScope,
        keyPath,
        value,
    };
}

export function objSetValue(pageScope, keyPath, value) {
    return {
        type: types.OBJ_SET_VALUE,
        pageScope,
        keyPath,
        value,
    };
}

export function objRemoveValue(pageScope, keyPath) {
    return {
        type: types.OBJ_REMOVE_VALUE,
        pageScope,
        keyPath,
    };
}

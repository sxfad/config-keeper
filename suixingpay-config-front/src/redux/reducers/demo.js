import {handleActions} from 'redux-actions';
import {handleAsyncReducer} from 'sx-ui';
import * as types from '../actionTypes';

let initialState = {
    message: 'demo init menssage',
};

export default handleActions({
    [types.DEMO]: handleAsyncReducer({
        always(state, action) {
            console.log('always', state, action);
            return state;
        },
        pending(state, action) {
            console.log('pending', state, action);
            return state;
        },
        resolve(state, action) {
            console.log('resolve', state, action);
            return state;
        },
        reject(state, action) {
            console.log('reject', state, action);
            return state;
        },
        complete(state, action) {
            console.log('complete', state, action);
            return state;
        },
    }),
}, initialState);

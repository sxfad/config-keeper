import {createStore, applyMiddleware, combineReducers} from 'redux';
import thunkMiddleware from 'redux-thunk';
import promiseMiddleware from './promise-middleware';
import asyncActionCallbackMiddleware from './async-action-callback-middleware';
import utilsMiddleware from './utils-middleware';
import syncReducerToAsyncStorage from './sync-reducer-to-local-storage-middleware';
import reducers from '../reducers';


let middlewares = [
    thunkMiddleware,
    promiseMiddleware,
    asyncActionCallbackMiddleware,
    utilsMiddleware,
    syncReducerToAsyncStorage,
];

export default function configureStore(initialState) {
    return applyMiddleware(
        ...middlewares
    )(createStore)(combineReducers(reducers), initialState);
}

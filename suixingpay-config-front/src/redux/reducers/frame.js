import {handleActions} from 'redux-actions';
import {handleAsyncReducer} from 'sx-ui';
import {convertToTree, getNodeByPropertyAndValue, getTopNodeByNode} from 'sx-ui/utils/tree-utils';
import {uniqueArray} from 'sx-ui/utils';
import * as types from '../actionTypes';
import {getMenuTreeData, setMenuTreeData} from '../../commons';

let initialState = {
    menuTreeData: [],
    sideBarMenuTreeData: [],
    currentTopMenuNode: {},
    currentSideBarMenuNode: {},
    menuOpenKeys: [],
    sideBarCollapsed: false,
    breadcrumbs: [],
    pageTitle: '',
    showPageHeader: true,
    showSideBar: true,
};

export default handleActions({
    [types.GET_SYSTEM_MENUS]: handleAsyncReducer({
        always(state, /* action */) {
            return state;
        },
        pending(state, /* action */) {
            return state;
        },
        resolve(state, action) {
            const {payload} = action;
            let menuTreeData = state.menuTreeData;

            if (payload && payload.length) {
                menuTreeData = convertToTree(payload);
            }
            setMenuTreeData(menuTreeData);

            return {
                ...state,
                menuTreeData,
            };
        },
        reject(state, /* action */) {
            return state;
        },
        complete(state, /* action */) {
            return state;
        },
    }),
    [types.SET_SYSTEM_MENUS_STATUS_BY_URL](state) {
        const menuTreeData = getMenuTreeData() || [];
        let currentSideBarMenuNode = {};
        let currentTopMenuNode = {};
        let menuOpenKeys = [...state.menuOpenKeys];
        let breadcrumbs = [];
        let pageTitle = '';

        if (menuTreeData) {
            let path = location.pathname;
            if (path.indexOf('/+') > -1) {
                path = path.substring(0, path.indexOf('/+'));
            }
            // TODO: 如果菜单节点的path 配置时就 携带参数， 只通过pathname无法匹配到菜单
            // TODO: 如果path=/user/:id,这种 通过pathname无法匹配到菜单
            currentSideBarMenuNode = getNodeByPropertyAndValue(menuTreeData, 'path', path);
            // 顶级节点有可能设置了某个子节点的path，会导致定位到顶级，而不是具体的菜单节点
            if (currentSideBarMenuNode && !currentSideBarMenuNode.parentKey) {
                currentSideBarMenuNode = getNodeByPropertyAndValue(currentSideBarMenuNode.children, 'path', path);
            }

            if (currentSideBarMenuNode) {
                pageTitle = currentSideBarMenuNode.text;
                const parentNodes = currentSideBarMenuNode.parentNodes;
                currentTopMenuNode = getTopNodeByNode(menuTreeData, currentSideBarMenuNode);

                // 保持其他打开的菜单
                menuOpenKeys = menuOpenKeys.concat(currentSideBarMenuNode.parentKeys);
                // 关闭其他菜单
                // menuOpenKeys = [...currentSideBarMenuNode.parentKeys];

                menuOpenKeys = uniqueArray(menuOpenKeys);
                if (parentNodes && parentNodes.length) {
                    breadcrumbs = parentNodes.concat(currentSideBarMenuNode);
                    breadcrumbs.unshift({
                        key: 'home-key',
                        text: '首页',
                        path: '/',
                        icon: 'fa-home',
                    });
                }
            }
        }
        return {
            ...state,
            menuTreeData,
            currentTopMenuNode,
            currentSideBarMenuNode,
            menuOpenKeys,
            breadcrumbs,
            pageTitle,
            showPageHeader: true,
            showSideBar: true,
        };
    },
    [types.SET_SYSTEM_MENU_OPEN_KEYS](state, action) {
        const {payload} = action;
        return {
            ...state,
            menuOpenKeys: payload,
        };
    },
    [types.TOGGLE_SIDE_BAR](state) {
        let {sideBarCollapsed} = state;
        return {
            ...state,
            sideBarCollapsed: !sideBarCollapsed,
        };
    },
    [types.SET_PAGE_TITLE](state, action) {
        const {payload} = action;
        return {
            ...state,
            pageTitle: payload,
        };
    },
    [types.SET_PAGE_BREADCRUMBS](state, action) {
        const {payload = []} = action;
        return {
            ...state,
            breadcrumbs: payload,
        };
    },
    [types.HIDE_PAGE_HEADER](state) {
        return {
            ...state,
            showPageHeader: false,
        };
    },
    [types.SHOW_PAGE_HEADER](state) {
        return {
            ...state,
            showPageHeader: true,
        };
    },
    [types.SHOW_SIDE_BAR](state) {
        return {
            ...state,
            showSideBar: true,
        };
    },
    [types.HIDE_SIDE_BAR](state) {
        return {
            ...state,
            showSideBar: false,
        };
    },
}, initialState);

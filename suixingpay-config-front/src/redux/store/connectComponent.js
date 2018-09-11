import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import actions from '../actions';


const options = {
    withRef: true,
};


export default function connectComponent(component) {
    const {
        INIT_STATE = {},
        mapStateToProps = () => ({}),
        mapDispatchToProps = (dispatch) => {
            const ac = bindActionCreators(actions, dispatch);
            const PAGE_SCOPE = INIT_STATE.scope || 'commonPageScope';
            ac.setState = (pageScope, payload) => {
                if (typeof pageScope === 'string') {
                    ac.setScopeState(pageScope, payload);
                } else {
                    ac.setScopeState(PAGE_SCOPE, pageScope);
                }
            };

            const fnMap = {
                objSet: 'objSetValue',
                objRemove: 'objRemoveValue',
                arrAppend: 'arrAppendValue',
                arrRemove: 'arrRemoveValue',
                arrRemoveAll: 'arrRemoveAllValue',
            };

            function simpleAction(newFn) {
                const oldFn = fnMap[newFn];
                ac[newFn] = (...args) => {
                    let pageScope = args[0];
                    let keyPath = args[1];
                    let value = args[2];
                    if (args.length === 2) {
                        pageScope = PAGE_SCOPE;
                        keyPath = args[0];
                        value = args[1];
                    }
                    ac[oldFn](pageScope, keyPath, value);
                };
            }

            Object.keys(fnMap).forEach(simpleAction);

            return {actions: ac};
        },
        mergeProps,
        LayoutComponent,
    } = component;

    if (mapStateToProps && LayoutComponent) {
        return connect(
            mapStateToProps,
            mapDispatchToProps,
            mergeProps,
            options
        )(LayoutComponent);
    }
    return LayoutComponent || component.default || component; // 如果 component有多个导出，优先LayoutComponent，其次使用默认导出
}

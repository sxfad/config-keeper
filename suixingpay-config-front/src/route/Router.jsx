import React, {Component} from 'react';
import {Router, browserHistory} from 'react-router';
import allPageRoutes from '../all-routes';
import connectComponent from '../redux/store/connectComponent.js';
import pageRoutes from '../page-routes';
import * as Frame from '../frame/Frame';
import * as Home from '../pages/home/Home';
import * as Error404 from '../pages/error/Error404';
import {toLogin, getCurrentLoginUser} from '../commons';

const currentLoginUser = getCurrentLoginUser();

class LayoutComponent extends Component {
    constructor(props) {
        super(props);
        const allRoutes = allPageRoutes.concat(pageRoutes);
        // 所有未截获请求，渲染Error404组件
        allRoutes.push(
            {
                path: '*',
                component: connectComponent(Error404),
            }
        );

        // 没找到统一的enter 和 leave回调，这里只能为每个route都添加
        allRoutes.forEach(r => {
            const oriOnEnter = r.onEnter;
            const oriOnLeave = r.onLeave;

            r.onEnter = (nextState, replace, callback) => {
                this.onEnter(nextState, replace, callback, oriOnEnter);
            };
            r.onLeave = (prevState) => {
                this.onLeave(prevState, oriOnLeave);
            };
        });

        this.routes = {
            path: '/',
            component: connectComponent(Frame),
            indexRoute: {
                component: connectComponent(Home),
                onEnter: this.onEnter,
                onLeave: this.onLeave,
            },
            childRoutes: allRoutes,
        };
        browserHistory.listen(() => {
            this.props.actions.setSystemMenusStatusByUrl();
        });
    }


    onLeave = (prevState, oriOnLeave) => {
        if (oriOnLeave) {
            oriOnLeave(prevState);
        }
    };

    onEnter = (nextState, replace, callback, oriOnEnter) => {
        const ignorePath = [
            '/error/401',
            '/error/403',
            '/error/404',
        ];
        const {location} = nextState;
        if (!currentLoginUser) {
            if (ignorePath.indexOf(location.pathname) < 0) {
                toLogin();
            }
        } else {
            callback();
        }

        if (oriOnEnter) {
            oriOnEnter(nextState, replace, callback);
        } else {
            const scrollDom = document.documentElement || document.body;
            scrollDom.scrollTop = 0;
            callback();
        }
    };

    // 这里可以注入通用props
    createElement = (RouteComponent, props) => {
        return (
            <RouteComponent {...props}/>
        );
    };

    render() {
        return (
            <Router
                routes={this.routes}
                history={browserHistory}
                createElement={this.createElement}
            />
        );
    }
}


function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

export default connectComponent({LayoutComponent, mapStateToProps});

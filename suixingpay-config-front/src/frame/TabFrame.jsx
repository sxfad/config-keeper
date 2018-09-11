import React, {Component} from 'react';
import {message, Tabs} from 'antd';
import {event} from 'sx-ui';
import {mosaicUrl} from 'sx-ui/utils';
import 'nprogress/nprogress.css';
import './style.less';
import handleErrorMessage from '../commons/handle-error-message';
import Header from './Header';
import SideBar from './SideBar';
import PageHeader from './PageHeader';
import {getCurrentLoginUser} from '../commons';

const TabPane = Tabs.TabPane;
const IFRAME_TYPE = 'iframe';
@event()
export class LayoutComponent extends Component {

    componentWillMount() {
        const {actions, $on} = this.props;
        actions.setSystemMenusStatusByUrl();
        actions.getStateFromStorage();

        $on('message', ({type, message: msg, error = {}}) => {
            if (type === 'error') {
                handleErrorMessage(error);
            } else if (type === 'success') {
                message.success(msg, 3);
            } else {
                message.info(msg, 3);
            }
        });
    }

    handleTabChange = (activeKey) => {
        const currentTab = this.tabs[activeKey];
        const path = currentTab.path;
        this.props.router.push(path);
    };
    handleTabEdit = (targetKey, action) => {
        if (action === 'remove') {
            // 最后一个不删除
            if (Object.keys(this.tabs).length <= 0) return;

            let prePath = location.pathname;
            // 关闭当前tab
            if (this.activeKey === targetKey) {
                let preIndex = 0;
                for (let i = 0; i < Object.keys(this.tabs).length; i++) {
                    if (Object.keys(this.tabs)[i] === targetKey) {
                        preIndex = i - 1;
                        break;
                    }
                }
                preIndex = preIndex < 0 ? 0 : preIndex;

                prePath = Object.keys(this.tabs)[preIndex];
            }
            this.props.router.push(prePath);

            Reflect.deleteProperty(this.tabs, targetKey);
        }
    };
    activeKey = 0;
    tabs = {};


    render() {
        const {sideBarCollapsed, showSideBar, showPageHeader, currentSideBarMenuNode, currentTopMenuNode} = this.props;
        const sideBarCollapsedWidth = 60;
        const sideBarExpendedWidth = 180;
        const headerHeight = 56;
        const tabHeight = 0;
        const pageHeaderHeight = 50;

        let paddingLeft = sideBarCollapsed ? sideBarCollapsedWidth : sideBarExpendedWidth;
        paddingLeft = showSideBar ? paddingLeft : 0;
        const paddingTop = showPageHeader ? headerHeight + tabHeight + pageHeaderHeight : headerHeight + tabHeight;


        const pathname = location.pathname;
        const childrenPathname = this.props.children && this.props.children.props.location.pathname;
        const activeKey = this.activeKey = pathname;

        const key = pathname;

        if (
            pathname === childrenPathname
            && !this.tabs[key]
        ) {
            this.tabs[key] = {
                key,
                name,
                path: pathname + location.search,
                component: this.props.children,
            };
        }

        let iframeContentStyle = {};
        if (currentSideBarMenuNode && currentSideBarMenuNode.url) {
            if (this.tabs[key] && this.tabs[key].type !== IFRAME_TYPE) {
                let url = currentSideBarMenuNode && currentSideBarMenuNode.url;
                if (currentSideBarMenuNode && currentSideBarMenuNode.connectType === '1') {
                    url = ((currentTopMenuNode && currentTopMenuNode.url) || '') + url;
                }
                const currentLoginUser = getCurrentLoginUser();
                url = mosaicUrl(url, {
                    token: currentLoginUser ? currentLoginUser.authToken : '',
                    isMenu: '1',
                });
                this.tabs[key].component = (
                    <iframe src={url} frameBorder={0} style={{width: '100%', height: '100%'}}/>
                );
                this.tabs[key].type = IFRAME_TYPE;
            }
            iframeContentStyle = {
                position: 'absolute',
                top: 0,
                right: 0,
                bottom: 0,
                left: 0,
            };
        }

        if (this.tabs[key]) {
            this.tabs[key].name = name;
        }

        return (
            <div className="app-frame">
                <Header/>
                <SideBar/>
                <PageHeader top={headerHeight + tabHeight}/>
                <div id="frame-content" className="frame-content" style={{paddingLeft, paddingTop, ...iframeContentStyle}}>
                    {
                        Object.keys(this.tabs).map(k => {
                            const item = this.tabs[k];
                            const style = {
                                display: item.key === activeKey ? 'block' : 'none',
                            };
                            if (item.type === IFRAME_TYPE) {
                                style.height = '100%';
                            }
                            return (
                                <div style={style} key={`tab-page-${item.key}`}>
                                    {item.component}
                                </div>
                            );
                        })
                    }
                </div>
            </div>
        );
    }
}

export function

mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

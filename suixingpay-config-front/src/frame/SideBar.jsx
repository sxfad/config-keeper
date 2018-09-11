import React, {Component} from 'react';
import {Link} from 'react-router';
import {Menu} from 'antd';
import {renderNode} from 'sx-ui/utils/tree-utils';
import {FontIcon} from 'sx-ui/antd';
import {getScrollBarWidth} from 'sx-ui/utils';
import Utils from '../commons/utils';
import connectComponent from '../redux/store/connectComponent';
import logoPic from './lg_logo.png';
import {browserHistory} from 'react-router';


const SubMenu = Menu.SubMenu;

class LayoutComponent extends Component {
    componentDidMount() {
        const {actions} = this.props;
        actions.toggleSideBar();
    }

    handleToggleSideBar = () => {
        const {actions} = this.props;
        actions.toggleSideBar();
    }
    handleOpenChange = (openKeys) => {
        const {actions} = this.props;
        actions.setSystemMenuOpenKeys(openKeys);
    }

    renderMenus() {
        const {currentTopMenuNode, sideBarCollapsed} = this.props;
        if (currentTopMenuNode && currentTopMenuNode.children) {
            if (sideBarCollapsed) {
                currentTopMenuNode.children.forEach(item => item.isTop = true);
            }
            return renderNode(currentTopMenuNode.children, (item, children) => {
                const isTop = item.isTop;
                const key = item.key;
                const path = item.path;
                const text = item.text;
                const icon = item.icon;
                let title = <span><FontIcon type={icon}/>{text}</span>;

                if (sideBarCollapsed && isTop) {
                    title = <span><FontIcon type={icon}/><span className="side-bar-top-menu-text">{text}</span></span>;
                }

                if (children) {
                    return (
                        <SubMenu key={key} title={title}>
                            {children}
                        </SubMenu>
                    );
                }
                return (
                    <Menu.Item key={key} style={{paddingLeft: 20}}>
                        <Link to={path} onClick={()=>{this.toPageLink()}}>
                            {title}
                        </Link>
                    </Menu.Item>
                );
            });
        }
    }

    toPageLink() {
        this.props.actions.setState('applicationConfig', {
            applicationPageNum: 1,
            applicationPageSize: 10,
            applicationApplicationName: '',
            applicationProfileName: '',
        });
        this.props.actions.setState('applicationHistoryList', {
            applicationHistoryPageNum: 1,
            applicationHistoryPageSize: 10,
            applicationHistoryMinV: '',
            applicationHistoryMaxV: '',
        });
        this.props.actions.setState('globalHistoryList', {
            globalHistoryPageNum: 1,
            globalHistoryPageSize: 10,
            globalHistoryMinV: '',
            globalHistoryMaxV: '',
        });
        this.props.actions.setState('userList', {
            userPageNum: 1,
            userPageSize: 10,
            userName: '',
            userStatus: '',
            userAdministrator: '',
        });
    }

    render() {
        let {currentSideBarMenuNode, currentTopMenuNode} = this.props;
        const {menuOpenKeys, sideBarCollapsed, showSideBar} = this.props;
        const sideBarWidth = sideBarCollapsed ? 60 : 180;
        const mode = sideBarCollapsed ? 'vertical' : 'inline';
        const outerOverFlow = sideBarCollapsed ? 'visible' : 'hidden';
        const innerOverFlow = sideBarCollapsed ? '' : 'scroll';
        const scrollBarWidth = getScrollBarWidth();
        const innerWidth = sideBarCollapsed ? sideBarWidth - 1 : (sideBarWidth + scrollBarWidth) - 1; // 1 为outer 的 border
        const logo = sideBarCollapsed ? <img src={logoPic} width="90%" alt="logo"/> : '随行付统一配置中心';
        const path = Utils.getRootUrl().path;
        if (!currentSideBarMenuNode) currentSideBarMenuNode = {};
        return (
            <div className="frame-side-bar" style={{width: sideBarWidth, display: showSideBar ? 'block' : 'none'}}>
                <div className="logo" style={{backgroundColor:'#404040'}}>
                    <Link to={path}>
                        {logo}
                    </Link>
                    <div className="side-bar-toggle" onClick={this.handleToggleSideBar}>
                        <FontIcon type="fa-bars"/>
                    </div>
                </div>
                <div className="menu-outer" style={{overflow: outerOverFlow}}>
                    <div className="menu-inner" style={{width: innerWidth, overflowY: innerOverFlow}}>
                        <Menu
                            theme="dark"
                            style={{display: sideBarCollapsed ? 'none' : 'block'}}
                            mode={mode}
                            selectedKeys={[currentSideBarMenuNode.key]}
                            openKeys={menuOpenKeys}
                            onOpenChange={this.handleOpenChange}
                        >
                            {this.renderMenus()}
                        </Menu>
                        <Menu
                            theme="dark"
                            style={{display: !sideBarCollapsed ? 'none' : 'block'}}
                            mode={mode}
                            selectedKeys={[currentSideBarMenuNode.key]}
                        >
                            {this.renderMenus()}
                        </Menu>
                    </div>
                </div>

            </div>
        );
    }
}

function mapStateToProps(state) {
    return {
        ...state.frame,
        ...state.pageState.applicationConfig,
        ...state.pageState.applicationHistoryList,
        ...state.pageState.globalHistoryList,
        ...state.pageState.userList,
    };
}

export default connectComponent({LayoutComponent, mapStateToProps});

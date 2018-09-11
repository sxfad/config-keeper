import React, {Component} from 'react';
import {Menu, Popconfirm} from 'antd';
import classNames from 'classnames';
import {FontIcon, UserAvatar} from 'sx-ui/antd';
import {Link, browserHistory} from 'react-router';
import {ajax} from 'sx-ui';
import {logout, getCurrentLoginUser} from '../commons';
import connectComponent from '../redux/store/connectComponent';
import {getFirstValue} from 'sx-ui/utils/tree-utils';
import Pass from '../pages/system/profile/Pass';

@ajax()
class LayoutComponent extends Component {
    state = {
        clickPassword: false,
    }
    handleLogoutPopVisibleChange = (visible) => {
        if (visible) {
            // 使弹框固定，不随滚动条滚动
            window.setTimeout(() => {
                const popover = document.querySelector('.ant-popover.ant-popover-placement-bottomRight');
                popover.style.top = '56px';
                popover.style.position = 'fixed';
            }, 0);
        }
    };

    handleLogout = () => {
        logout();
    };

    /**
     * 修改密码
     */
    rePassword = () => {
        this.setState({clickPassword: true});
    };
    /**
     * 弹窗消失
     * @param val
     */
    handleCancel = (val) => {
        this.setState({clickPassword: val});
    };

    test = (val) => {
    }

    renderMenus() {
        const {menuTreeData = []} = this.props;
        return menuTreeData.map(node => {
            const key = node.key;
            const path = getFirstValue(menuTreeData, node, 'path');
            const icon = node.icon;
            const text = node.text;
            return (
                <Menu.Item key={key}>
                    <Link to={path}>
                        <FontIcon type={icon} style={{fontSize: 16}}/>{text}
                    </Link>
                </Menu.Item>
            );
        });
    }

    render() {
        const {currentTopMenuNode= {}, sideBarCollapsed, showSideBar} = this.props;
        const frameHeaderClass = classNames({
            'side-bar-collapsed': sideBarCollapsed,
            'side-bar-hidden': !showSideBar,
        });
        const frameHeaderMenu = showSideBar ? 'none' : '';
        const user = getCurrentLoginUser() ||
            {
                name: '匿名',
                loginName: 'no name',
                avatar: '',
            };
        return (
            <div className={`frame-header ${frameHeaderClass}`}>
                <div className={`left-menu ${frameHeaderClass}`} style={{display: frameHeaderMenu}}>
                    <Menu
                        selectedKeys={[currentTopMenuNode.key]}
                        mode="horizontal"
                    >
                        {this.renderMenus()}
                    </Menu>
                </div>
                <div className="right-menu">
                    <div className="right-menu-item" onClick={() => this.rePassword()}>
                        <UserAvatar user={user}/>
                        <span>{user.name}</span>
                    </div>
                    {this.state.clickPassword ? <Pass test={this.test} clickPassword={this.state.clickPassword} handleCancel={this.handleCancel}/> : ''}
                    <Popconfirm
                        onVisibleChange={this.handleLogoutPopVisibleChange}
                        placement="bottomRight"
                        title="您确定要退出系统吗？"
                        onConfirm={this.handleLogout}
                    >
                        <div className="right-menu-item">
                            <FontIcon type="logout" size="lg"/>
                        </div>
                    </Popconfirm>
                </div>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

export default connectComponent({LayoutComponent, mapStateToProps});

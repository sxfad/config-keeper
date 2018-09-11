import React, {Component} from 'react';
import {Button} from 'antd';
import {Link} from 'react-router';
import {addClass, removeClass, mosaicUrl} from 'sx-ui/utils';
import {getCurrentLoginUser} from '../../commons';
import error404 from './404.png';
import './style.less';

/**
 * 页面未找到分为两种情况：
 * 1. path配置错误，前端没有对应的route
 * 2. 菜单中有url，为打开iframe菜单，前端本身就没有配置route
 */
export class LayoutComponent extends Component {
    state = {
        url: null,
    };

    componentWillReceiveProps(nextProps) {
        const {currentSideBarMenuNode, currentTopMenuNode} = nextProps;
        this.handleIframe(currentTopMenuNode, currentSideBarMenuNode);
    }

    componentWillMount() {
        const {currentSideBarMenuNode, currentTopMenuNode} = this.props;
        this.handleIframe(currentTopMenuNode, currentSideBarMenuNode);
    }

    handleIframe(currentTopMenuNode, currentSideBarMenuNode) {
        let url = currentSideBarMenuNode && currentSideBarMenuNode.url;
        url = ((currentTopMenuNode && currentTopMenuNode.url) || '') + url;
        const currentLoginUser = getCurrentLoginUser();
        url = mosaicUrl(url, {
            token: currentLoginUser ? currentLoginUser.authToken : '',
            isMenu: '1',
        });
        if (url) {
            this.setState({url});
            addClass('#frame-content', 'frame-content-iframe');
        } else {
            removeClass('#frame-content', 'frame-content-iframe');
            this.setState({url: null});
        }
    }

    componentWillUnmount() {
    }

    render() {
        const {url} = this.state;
        const {sideBarCollapsed} = this.props;
        const left = sideBarCollapsed ? 60 : 200;
        if (url) {
            return (
                <div className="iframe-content" style={{left}}>
                    <iframe src={url} frameBorder={0} style={{width: '100%', height: '100%'}}/>
                </div>
            );
        }
        return (
            <div className="error-page">
                <img src={error404} alt="404图片"/>
                <p className="error-text">您访问的页面不存在...</p>
                <Button
                    type="primary"
                    className="error-btn"
                    onClick={this.props.router.goBack}
                >
                    返回上一级
                </Button>
                <Button
                    type="primary"
                    className="error-btn error-btn-right"
                >
                    <Link to="/">返回首页</Link>
                </Button>
            </div>
        );
    }
}

export function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

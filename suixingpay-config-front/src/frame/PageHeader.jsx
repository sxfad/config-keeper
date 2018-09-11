import React, {Component} from 'react';
import {Breadcrumb} from 'antd';
import {Link} from 'react-router';
import {FontIcon} from 'sx-ui/antd';
import connectComponent from '../redux/store/connectComponent';

class LayoutComponent extends Component {
    componentDidMount() {

    }

    renderBreadcrumb() {
        const {breadcrumbs = []} = this.props;
        return breadcrumbs.map(item => {
            const key = item.key;
            const path = item.path;
            const text = item.text;
            const icon = item.icon;
            if (path !== '/') {
                return (
                    <Breadcrumb.Item key={key}>
                        <Link to={path}>
                            <FontIcon type={icon}/><span>{text}</span>
                        </Link>
                    </Breadcrumb.Item>
                );
            }
        });
    }

    render() {
        const {pageTitle, showPageHeader, sideBarCollapsed, showSideBar, top} = this.props;
        let className = sideBarCollapsed ? 'side-bar-collapsed' : '';
        className = showSideBar ? className : `${showSideBar} side-bar-hidden`;

        const style = {display: showPageHeader ? 'block' : 'none'};
        if (top) {
            style.top = top;
        }
        return (
            <div className={`page-header ${className}`} style={style}>
                <h1>{pageTitle}</h1>
                <Breadcrumb>
                    {this.renderBreadcrumb()}
                </Breadcrumb>
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

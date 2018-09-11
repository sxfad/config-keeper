import React from 'react';

export const PAGE_ROUTE = '/';

export class LayoutComponent extends React.Component {
    state = {
        startDate: '',
        endDate: '',
        startValue: null,
        endValue: null,
        endOpen: false,
    };

    componentWillMount() {
        const {actions} = this.props;
        // actions.hidePageHeader();
        // actions.hideSideBar();
        actions.setPageTitle('首页');
        actions.setPageBreadcrumbs([
            {
                key: 'home',
                path: '',
                text: '首页',
                icon: 'home',
            },
        ]);
    }

    onChange = (field, value) => {
        this.setState({
            [field]: value,
        });
    }

    render() {
        return (
            <h1 style={{textAlign: 'center', marginTop: 100}}>
                欢迎使用随行付统一配置中心
            </h1>
        )
    }
}

// export default Home;
export function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}
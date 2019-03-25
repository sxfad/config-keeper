import React, {Component} from 'react';
import {Form, Button, Table} from 'antd';
import {PageContent, Operator, QueryResult, ToolBar} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import {browserHistory} from 'react-router';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

const FormItem = Form.Item;

export const PAGE_ROUTE = '/base-information/global-config';

class globalConfig extends Component {
    state = {
        gettingGlobalConfig: false,
        globalConfigs: [],
        addEdit: false,
        detail: false,
        history: false,
        globalConfigId: '',
        global: '',
        profile: '',
    };

    columns = [
        {
            title: '#',
            render: (text, record, index) => (index + 1),
        },
        {
            title: '环境',
            dataIndex: 'profile',
            key: 'profile',
            render: (record) => {
                return (
                    <span>{record.name}({record.profile})</span>
                );
            },
        },
        {
            title: '配置类型',
            dataIndex: 'sourceType',
            key: 'sourceType',
            render: (text, record) => {
                if (record.sourceType === 'PROPERTIES') {
                    return (
                        <span>PROPERTIES</span>
                    );
                } else {
                    return (
                        <span>YAML</span>
                    );
                }
            },
        },
        {
            title: '备注',
            dataIndex: 'memo',
            key: 'memo',
        },
        {
            title: '修改人',
            dataIndex: 'user.name',
            key: 'user.name',
        },
        {
            title: '修改时间',
            dataIndex: 'modifyTime',
            key: 'modifyTime',
            render: (record) => {
                let modifyTime = new Date(record).toLocaleString();
                return (
                    <span>{modifyTime.split("/").join('-')}</span>
                );
            },
        },
        {
            title: '版本号',
            dataIndex: 'version',
            key: 'version',
        },
        {
            title: '操作',
            key: 'operator',
            render: (text, record) => {
                const items = [
                    {
                        label: '查看',
                        onClick: () => this.handleDetail(record),
                    },
                    {
                        label: '修改',
                        onClick: () => this.handleEdit(record),
                    },

                    {
                        label: '历史版本',
                        onClick: () => this.handleHistory(record),
                    },
                ];

                return (<Operator items={items}/>);
            },
        },
    ];

    search = (args) => {
        const {form: {getFieldsValue}} = this.props;
        const {pageNum, pageSize} = this.state;
        let params = {
            ...getFieldsValue(),
            pageNum,
            pageSize,
            ...args,
        };
        this.setState({
            gettingGlobalConfig: true,
        });

        promiseAjax.get('/globalconfig', params).then(rsp => {
            if (rsp.data !== undefined) {
                this.setState({
                    pageSize: rsp.data.size,
                    total: rsp.data.numberOfElements,
                    globalConfigs: rsp.data,
                });
            } else {
                this.setState({
                    globalConfigs: [],
                });
            }
        }).finally(() => {
            this.setState({
                gettingGlobalConfig: false,
            });
        });
    };

    /**
     *  添加
     */
    handleAdd = () => {
        // this.setState({
        //     addEdit: true,
        //     global: 'addApplcation',
        //     profile: 'addProfile'
        // });
        // this.showModal();
        browserHistory.push(`/base-information/global-config/+add/addProfile`);
    };

    /**
     * 历史版本
     * @param record
     */
        // handleHistory = (record) => {
        //     browserHistory.push(`/base-information/application-config/+history/${record.profile.profile}/${record.application.name}`);
        // };
    handleHistory = (record) => {
        // this.setState({
        //     history: true,
        //     addEdit: false,
        //     detail: false,
        //     profile: record.profile.profile,
        // });
        browserHistory.push(`/base-information/global-config/+history/${record.profile.profile}`);
    };

    /**
     * 详情
     */
    handleDetail = (record) => {
        // this.setState({
        //     detail: true,
        //     addEdit: false,
        //     history: false,
        //     profile: record.profile.profile,
        // });
        browserHistory.push(`/base-information/global-config/+detail/${record.profile.profile}/historyId`);
    };

    handleSave = (val) => {
        this.setState({
            addEdit: val,
            detail: val,
            history: val,
        });
        this.search();
    };

    /**
     * 修改
     * @param record
     */
    handleEdit = (record) => {
        // this.setState({
        //     addEdit: true,
        //     detail: false,
        //     history: false,
        //     profile: record.profile.profile,
        // });
        browserHistory.push(`/base-information/global-config/+add/${record.profile.profile}`);
    };

    /**
     * 查询
     * @param e
     */
    handleQuery = (e) => {
        e.preventDefault();
        this.search();
    };

    componentDidMount() {
        this.props.actions.setState('globalHistoryList', {
            globalHistoryPageNum: 1,
            globalHistoryPageSize: 10,
            globalHistoryMinV: '',
            globalHistoryMaxV: '',
        });
        // 页面渲染完成，进行一次查询
        this.search();
    }

    render() {
        const {
            gettingGlobalConfig,
            globalConfigs,
        } = this.state;
        return (
            <div>
                <PageContent>
                    <ToolBar style={{textAlign: 'right'}}>
                        <Button type="primary" onClick={this.handleAdd}>添加</Button>
                    </ToolBar>
                    <QueryResult>
                        <Table
                            loading={gettingGlobalConfig}
                            size="middle"
                            rowKey={(record) => record.modifyTime}
                            columns={this.columns}
                            dataSource={globalConfigs}
                            pagination={false}
                        />
                    </QueryResult>
                </PageContent>
            </div>
        );
    }
}

// export default Form.create()(globalConfig);
export function mapStateToProps(state) {
    return {
        ...state.frame,
        ...state.pageState.globalHistoryList,
    };
}

const LayoutComponent = Form.create()(globalConfig);
export default connectComponent({LayoutComponent, mapStateToProps});

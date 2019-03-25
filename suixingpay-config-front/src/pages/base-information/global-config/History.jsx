import React, {Component} from 'react';
import {Form, Input, Button, Table, Row, Col, message} from 'antd';
import {PageContent, Operator, QueryBar, QueryResult, PaginationComponent, ToolBar} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import GlobalConfigDetail from './Detail';
import {browserHistory} from 'react-router';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

export const PAGE_ROUTE = '/base-information/global-config/+history/:profile';

const FormItem = Form.Item;
export const INIT_STATE = {
    scope: 'globalHistoryList',
    sync: false,
    globalHistoryPageNum: '',
    globalHistoryPageSize: '',
    globalHistoryMinV: '',
    globalHistoryMaxV: '',
};

class GlobalConfig extends Component {
    state = {
        pageNum: 1,
        pageSize: 10,
        gettingGlobalConfig: false,
        allGlobalConfigs: [],
        total: 0,
        detail: false,
        historyId: '',
        globalConfigId: '',
        profile: '',
        selectedRowKeys: [],
        record: {},
    };

    columns = [
        {
            title: '#',
            render: (text, record, index) => (index + 1) + ((this.state.pageNum - 1) * this.state.pageSize),
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
            title: '创建时间',
            dataIndex: 'createdDate',
            key: 'createdDate',
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
                        label: '替换',
                        confirm: {
                            title: '你确定还原至当前版本吗？',
                            onConfirm: () => this.handleReplace(record),
                        },
                    },
                    this.state.selectedRowKeys.length >= 2 ?
                        {
                            label: <span style={{color: '#999'}}>对比</span>,
                        } : {
                            label: <span>对比</span>,
                            onClick: () => this.handleDiff(record),
                        },
                ];

                return (<Operator items={items}/>);
            },
        },
    ];

    /**
     * 版本对比
     * @param record
     */
    handleDiff = (record) => {
        browserHistory.push(`/base-information/global-config/+diff/${record.profile.profile}/${record.id}`);
    };

    search = (args) => {
        const {params: {profile}, form: {getFieldsValue, getFieldValue}} = this.props;
        const {pageNum, pageSize} = this.state;

        this.props.actions.setState('globalHistoryList', {
            globalHistoryPageNum: args.pageNum,
            globalHistoryPageSize: pageSize,
            globalHistoryMinV: getFieldValue('minVersion'),
            globalHistoryMaxV: getFieldValue('maxVersion'),
        });

        let params = {
            ...getFieldsValue(),
            pageNum,
            pageSize,
            profileName: profile,
            ...args,
        };
        this.setState({
            gettingGlobalConfig: true,
        });
        promiseAjax.get('/globalconfiglog', params).then(rsp => {
            if (rsp.data !== undefined) {
                this.setState({
                    pageNum: rsp.data.number + 1,
                    pageSize: rsp.data.size,
                    total: rsp.data.totalElements,
                    globalConfigs: rsp.data.content,
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
    }

    /**
     * 详情
     * @param record
     */
    handleDetail = (record) => {
        // this.setState({
        //     detail: true,
        //     historyId: record.id,
        //     profile: record.profile.profile,
        // });
        browserHistory.push(`/base-information/global-config/+detail/${record.profile.profile}/${record.id}`);
    };

    /**
     * 替换
     * @param record
     */
    handleReplace = (record) => {
        promiseAjax.put(`/globalconfig/${record.id}`).then(rsp => {
            if (rsp.status === 'SUCCESS') {
                message.success('替换成功', 3);
                window.history.back();
            }
            // this.props.handleSave(false)
        })
    };

    handleSave = (val) => {
        this.setState({
            detail: val,
        });
        this.search();
    }

    /**
     * 查询
     * @param e
     */
    handleQuery = (e) => {
        e.preventDefault();
        this.search({
            pageNum: 1,
        });
    };

    handlePageNumChange = (pageNum) => {
        this.setState({
            pageNum,
        });
        this.search({
            pageNum,
        });
    };

    handlePageSizeChange = pageSize => {
        this.setState({
            pageNum: 1,
            pageSize,
        });
        this.search({
            pageNum: 1,
            pageSize,
        });
    };

    handleFirstRecord = (firstId) => {
        const { globalConfigs } = this.state;
        const record = globalConfigs.find(item => item.id === firstId);
        this.setState({record});
    }

    handleRowSelected = (selectedRowKeys,) => {
        if(selectedRowKeys.length === 1){
            const firstId = selectedRowKeys[0];
            this.handleFirstRecord(firstId);
        }
        if (selectedRowKeys && selectedRowKeys.length > 2) {
            return message.info('最多选择两条记录进行对比');
        }
        this.setState({selectedRowKeys});
    };

    handleCompare = () => {
        const {selectedRowKeys, record} = this.state;
        const secondId = selectedRowKeys[1] || '';
        browserHistory.push(`/base-information/global-config/+diff/${record.profile.profile}/${record.id}?secondId=${secondId}`);
    };

    componentWillMount() {
        // 页面渲染完成，进行一次查询
        const {params: {profile}, globalHistoryPageNum, globalHistoryPageSize, globalHistoryMinV, globalHistoryMaxV} = this.props;
        let newPageNum = (globalHistoryPageNum === undefined || globalHistoryPageNum === '') ? 1 : globalHistoryPageNum,
            newPageSize = (globalHistoryPageSize === undefined || globalHistoryPageSize === '') ? 10 : globalHistoryPageSize,
            newMinV = (globalHistoryMinV === undefined || globalHistoryMinV === '') ? '' : globalHistoryMinV,
            newMaxV = (globalHistoryMaxV === undefined || globalHistoryMaxV === '') ? '' : globalHistoryMaxV;
        let params = {
            minVersion: newMinV,
            maxVersion: newMaxV,
            pageNum: newPageNum,
            pageSize: newPageSize,
            profileName: profile,
        };

        this.setState({
            gettingGlobalConfig: true,
        });
        promiseAjax.get('/globalconfiglog', params).then(rsp => {
            if (rsp.data !== undefined) {
                this.setState({
                    pageNum: rsp.data.number + 1,
                    pageSize: rsp.data.size,
                    total: rsp.data.totalElements,
                    globalConfigs: rsp.data.content,
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
    }

    /**
     * 返回
     *
     * */
    goBack = () => {
        window.history.back();
    };

    /**
     * 重置表单
     */
    handleResetFields() {
        this.props.actions.setState('globalHistoryList', {
            globalHistoryPageNum: 1,
            globalHistoryPageSize: 10,
            globalHistoryMinV: '',
            globalHistoryMaxV: '',
        });
        this.props.form.resetFields();
    }

    render() {
        const {
            gettingGlobalConfig,
            globalConfigs,
            total,
            pageNum,
            pageSize,
            selectedRowKeys,
        } = this.state;
        const {form: {getFieldDecorator}} = this.props;
        const formItemLayout = {
            labelCol: {
                xs: {span: 24},
                sm: {span: 8},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },
        };
        const queryItemLayout = {
            xs: 7,
            md: 6,
            lg: 6,
        };
        const btnItemLayout = {
            xs: 10,
            md: 12,
            lg: 12,
        };
        return (
            <div>
                {
                    this.state.detail ?
                        (
                            <GlobalConfigDetail
                                profile={this.state.profile}
                                historyId={this.state.historyId}
                                handleSave={this.handleSave}
                            />
                        ) : ''
                }

                {this.state.detail ? '' : <PageContent>
                    <QueryBar>
                        <Form onSubmit={this.handleQuery}>
                            <Row gutter={16}>
                                <Col {...queryItemLayout}>
                                    <FormItem
                                        {...formItemLayout}
                                        label="最小版本号">
                                        {getFieldDecorator('minVersion', {
                                            initialValue: this.props.globalHistoryMinV,
                                        })(
                                            <Input placeholder="请输入最小版本号" style={{width: '100%'}}/>
                                        )}
                                    </FormItem>
                                </Col>
                                <Col {...queryItemLayout}>
                                    <FormItem
                                        {...formItemLayout}
                                        label="最大版本号">
                                        {getFieldDecorator('maxVersion', {
                                            initialValue: this.props.globalHistoryMaxV,
                                        })(
                                            <Input placeholder="请输入最大版本号" style={{width: '100%'}}/>
                                        )}
                                    </FormItem>
                                </Col>

                                <Col {...btnItemLayout} style={{textAlign: 'left', paddingLeft: 30}}>
                                    <FormItem
                                        colon={false}
                                        {...formItemLayout}
                                    >
                                        <Button
                                            type="primary"
                                            htmlType="submit"
                                            loading={gettingGlobalConfig}
                                            style={{marginRight: '16px'}}
                                        >查询</Button>
                                        <Button
                                            type="ghost"
                                            onClick={() => this.handleResetFields()}
                                            style={{marginRight: '16px'}}
                                        >重置</Button>
                                        <Button type="primary" onClick={this.goBack}>返回</Button>
                                    </FormItem>
                                </Col>
                            </Row>
                        </Form>
                    </QueryBar>
                    <ToolBar>
                        <Button
                            disabled={selectedRowKeys && selectedRowKeys.length < 1}
                            type="primary"
                            onClick={this.handleCompare}
                        >对比</Button>
                    </ToolBar>
                    <QueryResult>
                        <Table
                            rowSelection={{selectedRowKeys, onChange: this.handleRowSelected}}
                            loading={gettingGlobalConfig}
                            size="middle"
                            // rowKey={(record) => record.modifyTime}
                            rowKey={(record) => record.id}
                            columns={this.columns}
                            dataSource={globalConfigs}
                            pagination={false}
                        />
                    </QueryResult>
                    <PaginationComponent
                        pageSize={pageSize}
                        pageNum={pageNum}
                        total={total}
                        onPageNumChange={this.handlePageNumChange}
                        onPageSizeChange={this.handlePageSizeChange}
                    />
                </PageContent>
                }
            </div>
        );
    }
}

// export default Form.create()(GlobalConfig);
function mapStateToProps(state) {
    return {
        ...state.frame,
        ...state.pageState.globalHistoryList,
    };
}

const LayoutComponent = Form.create()(GlobalConfig);
export default connectComponent({LayoutComponent, mapStateToProps});

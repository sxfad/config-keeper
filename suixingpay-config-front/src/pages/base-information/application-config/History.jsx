import React, {Component} from 'react';
import {Form, Input, Button, Table, Row, Col, message} from 'antd';
import {PageContent, Operator, QueryBar, QueryResult, ToolBar, PaginationComponent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import ApplicationConfigDetail from './Detail';
import connectComponent from '../../../redux/store/connectComponent';
import {browserHistory} from 'react-router';
import './style.less';

export const PAGE_ROUTE = '/base-information/application-config/+history/:profile/:application';

const FormItem = Form.Item;
export const INIT_STATE = {
    scope: 'applicationHistoryList',
    sync: false,
    applicationHistoryPageNum: '',
    applicationHistoryPageSize: '',
    applicationHistoryMinV: '',
    applicationHistoryMaxV: '',
};

class ApplicationConfig extends Component {
    state = {
        pageNum: 1,
        pageSize: 10,
        gettingApplicationConfig: false,
        applicationConfigs: [],
        total: 0,
        detail: false,
        historyId: '',
        applicationConfigId: '',
        application: '',
        profile: '',
        selectedRowKeys: [],
        record: {},
    };

    columns = [
        {
            title: '#',
            key: '__num__',
            render: (text, record, index) => (index + 1) + ((this.state.pageNum - 1) * this.state.pageSize),
        },
        {
            title: '应用名称',
            dataIndex: 'application',
            key: 'application',
            render: (record) => {
                const name = record.name;
                const description = record.description;
                return (
                    <span>{description}({name})</span>
                );
            },
        },
        {
            title: '环境',
            dataIndex: 'profile',
            key: 'profile',
            render: (record) => {
                const name = record.name;
                const profile = record.profile;
                return (
                    <span>{name}({profile})</span>
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

    search = (args) => {
        const {params: {profile, application}} = this.props;
        const {form: {getFieldsValue, getFieldValue}} = this.props;
        const {pageNum, pageSize} = this.state;

        this.props.actions.setState('applicationHistoryList', {
            applicationHistoryPageNum: args.pageNum,
            applicationHistoryPageSize: pageSize,
            applicationHistoryMinV: getFieldValue('minVersion'),
            applicationHistoryMaxV: getFieldValue('maxVersion'),
        });

        let params = {
            ...getFieldsValue(),
            pageNum,
            pageSize,
            profileName: profile,
            applicationName: application,
            ...args,
        };
        this.setState({
            gettingApplicationConfig: true,
        });
        promiseAjax.get(`/applicationconfiglog/`, params).then(rsp => {
            if (rsp.data !== undefined) {
                this.setState({
                    pageNum: rsp.data.number + 1,
                    pageSize: rsp.data.size,
                    total: rsp.data.totalElements,
                    applicationConfigs: rsp.data.content,
                });
            } else {
                this.setState({
                    applicationConfigs: [],
                });
            }
        }).finally(() => {
            this.setState({
                gettingApplicationConfig: false,
            });
        });
    };

    /**
     * 详情
     */
    handleDetail = (record) => {
        browserHistory.push(`/base-information/application-config/+detail/${record.profile.profile}/${record.application.name}/${record.id}`);
    };

    /**
     * 替换
     * @param record
     */
    handleReplace = (record) => {
        promiseAjax.put(`/applicationconfig/${record.id}`).then(rsp => {
            if (rsp.status === 'SUCCESS') {
                message.success('替换成功', 3);
                window.history.back();
            }
            // this.props.handleSave(false)
        })
    };

    /**
     * 版本对比
     * @param record
     */
    handleDiff = (record) => {
        browserHistory.push(`/base-information/application-config/+diff/${record.profile.profile}/${record.application.name}/${record.id}`);
    };

    handleSave = (val) => {
        this.setState({
            detail: val,
        });
        this.search();
    };

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

    componentWillMount() {
        // 页面渲染完成，进行一次查询
        // this.search();
        const {params: {profile, application}, applicationHistoryPageNum, applicationHistoryPageSize, applicationHistoryMinV, applicationHistoryMaxV} = this.props;
        let newPageNum = (applicationHistoryPageNum === undefined || applicationHistoryPageNum === '') ? 1 : applicationHistoryPageNum,
            newPageSize = (applicationHistoryPageSize === undefined || applicationHistoryPageSize === '') ? 10 : applicationHistoryPageSize,
            newMinV = (applicationHistoryMinV === undefined || applicationHistoryMinV === '') ? '' : applicationHistoryMinV,
            newMaxV = (applicationHistoryMaxV === undefined || applicationHistoryMaxV === '') ? '' : applicationHistoryMaxV;

        let params = {
            minVersion: newMinV,
            maxVersion: newMaxV,
            pageNum: newPageNum,
            pageSize: newPageSize,
            profileName: profile,
            applicationName: application,
        };
        this.setState({
            gettingApplicationConfig: true,
        });
        promiseAjax.get(`/applicationconfiglog/`, params).then(rsp => {
            if (rsp.data !== undefined) {
                this.setState({
                    pageNum: rsp.data.number + 1,
                    pageSize: rsp.data.size,
                    total: rsp.data.totalElements,
                    applicationConfigs: rsp.data.content,
                });
            } else {
                this.setState({
                    applicationConfigs: [],
                });
            }
        }).finally(() => {
            this.setState({
                gettingApplicationConfig: false,
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
        this.props.actions.setState('applicationHistoryList', {
            applicationHistoryPageNum: 1,
            applicationHistoryPageSize: 10,
            applicationHistoryMinV: '',
            applicationHistoryMaxV: '',
        });
        this.props.form.resetFields();
    }

    handleFirstRecord = (firstId) => {
        const { applicationConfigs } = this.state;
        const record = applicationConfigs.find(item => item.id === firstId);
        this.setState({record});
    }

    handleRowSelected = (selectedRowKeys) => {
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
        browserHistory.push(`/base-information/application-config/+diff/${record.profile.profile}/${record.application.name}/${record.id}?secondId=${secondId}`);
    };

    render() {
        const {
            gettingApplicationConfig,
            applicationConfigs,
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
                            <ApplicationConfigDetail
                                application={this.state.application}
                                profile={this.state.profile}
                                historyId={this.state.historyId}
                                handleSave={this.handleSave}
                            />
                        ) : ''
                }

                {this.state.detail ? '' : <PageContent className="application-config">
                    <QueryBar>
                        <Form onSubmit={this.handleQuery}>
                            <Row gutter={16}>
                                <Col {...queryItemLayout}>
                                    <FormItem
                                        {...formItemLayout}
                                        label="最小版本号">
                                        {getFieldDecorator('minVersion', {
                                            initialValue: this.props.applicationHistoryMinV,
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
                                            initialValue: this.props.applicationHistoryMaxV,
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
                                            loading={gettingApplicationConfig}
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
                            loading={gettingApplicationConfig}
                            size="middle"
                            rowKey={(record) => record.id}
                            columns={this.columns}
                            dataSource={applicationConfigs}
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

// export default Form.create()(ApplicationConfig);
function mapStateToProps(state) {
    return {
        ...state.frame,
        ...state.pageState.applicationConfig,
        ...state.pageState.applicationHistoryList,
    };
}

const LayoutComponent = Form.create()(ApplicationConfig);
export default connectComponent({LayoutComponent, mapStateToProps});


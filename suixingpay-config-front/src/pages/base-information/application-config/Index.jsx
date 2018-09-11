import React, {Component} from 'react';
import {Form, Button, Table, Select, Row, Col} from 'antd';
import {PageContent, Operator, QueryBar, QueryResult, PaginationComponent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import {browserHistory} from 'react-router';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

const FormItem = Form.Item;
const Option = Select.Option;
let timeout;
let currentValue;
export const INIT_STATE = {
    scope: 'applicationConfig',
    sync: false,
    applicationPageNum: '',
    applicationPageSize: '',
    applicationApplicationName: '',
    applicationProfileName: '',
    applicationRefresh: true,
};

export const PAGE_ROUTE = '/base-information/application-config';

class ApplicationConfig extends Component {
    state = {
        pageNum: 1,
        pageSize: 10,
        gettingApplicationConfig: false,
        applicationConfigs: [],
        total: 0,
        addEdit: false,
        detail: false,
        history: false,
        applicationConfigId: '',
        application: '',
        profile: '',
        applicationName: '',
        profileName: '',
        profiles: [],
        applicationData: [],
        value: '',
    };

    columns = [
        {
            title: '#',
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

    componentWillMount() {
        promiseAjax.get('applicationconfig/profiles').then(rsp => {
            this.setState({
                profiles: rsp.data,
            })
        });

        const {applicationPageNum, applicationPageSize, applicationApplicationName, applicationProfileName} = this.props;
        let newPageNum = (applicationPageNum === undefined || applicationPageNum === '') ? 1 : applicationPageNum,
            newPageSize = (applicationPageSize === undefined || applicationPageSize === '') ? 10 : applicationPageSize,
            newApplicationName = (applicationApplicationName === undefined || applicationApplicationName === '') ? '' : applicationApplicationName,
            newProfileName = (applicationProfileName === undefined || applicationProfileName === '') ? '' : applicationProfileName;

        let params = {
            applicationName: newApplicationName,
            profileName: newProfileName,
            pageNum: newPageNum,
            pageSize: newPageSize,
        };
        this.setState({
            gettingApplicationConfig: true,
        });
        promiseAjax.get('/applicationconfig', params).then(rsp => {
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

    componentDidMount() {
        this.props.actions.setState('applicationHistoryList', {
            applicationHistoryPageNum: 1,
            applicationHistoryPageSize: 10,
            applicationHistoryMinV: '',
            applicationHistoryMaxV: '',
        });
        document.getElementById('addeditform').getElementsByTagName('form')[0].setAttribute('autocomplete', 'off');
    }

    search = (args) => {
        const {pageNum, pageSize} = this.state;
        const {getFieldValue} = this.props.form;
        let profile = getFieldValue('profile');
        let application = getFieldValue('application');

        this.props.actions.setState('applicationConfig', {
            applicationPageNum: args.pageNum,
            applicationPageSize: pageSize,
            applicationApplicationName: application,
            applicationProfileName: profile,
        });

        let params = {
            applicationName: application,
            profileName: profile,
            pageNum,
            pageSize,
            ...args,
        };
        this.setState({
            gettingApplicationConfig: true,
        });
        promiseAjax.get('/applicationconfig', params).then(rsp => {
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
     *  添加
     */
    handleAdd = () => {
        browserHistory.push(`/base-information/application-config/+add/addProfile/addApplcation`);
    };

    /**
     * 历史版本
     * @param record
     */
    handleHistory = (record) => {
        browserHistory.push(`/base-information/application-config/+history/${record.profile.profile}/${record.application.name}`);
    };

    /**
     * 详情
     */
    handleDetail = (record) => {
        browserHistory.push(`/base-information/application-config/+detail/${record.profile.profile}/${record.application.name}/historyId`);
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
        browserHistory.push(`/base-information/application-config/+add/${record.profile.profile}/${record.application.name}`);
    };

    handlePageNumChange = (pageNum) => {
        this.setState({
            pageNum,
        });
        this.search({
            pageNum,
        });
    };

    handlePageSizeChange = (pageSize) => {
        this.setState({
            pageNum: 1,
            pageSize,
        });
        this.search({
            pageNum: 1,
            pageSize,
        });
    };

    /**
     * 选择应用系统
     * @param applicationName
     */
    handleApplicationChange = (applicationName) => {
        this.setState({
            applicationName,
        });
    };

    /**
     * 选择环境
     * @param profileName
     */
    handleProfileChange = (profileName) => {
        this.setState({
            profileName: profileName,
        });
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

    /**
     * 重置表单
     */
    handleResetFields() {
        this.props.actions.setState('applicationConfig', {
            applicationPageNum: 1,
            applicationPageSize: 10,
            applicationApplicationName: '',
            applicationProfileName: '',
        });
        this.setState({
            applicationName: '',
            profileName: '',
        });
        this.props.form.resetFields();
    }

    /**
     * 初始化环境下拉菜单
     */
    renderProfileOptions = () => {
        return this.state.profiles.map(item => {
            const keyStr = item.name + '(' + item.profile + ')';
            return <Option key={item.profile}>{keyStr}</Option>;
        });
    };

    fetch(value) {
        if (timeout) {
            clearTimeout(timeout);
            timeout = null;
        }
        currentValue = value;
        let profile = 'undefined';
        if (this.props.profile !== undefined && this.props.profile !== '') {
            profile = this.props.profile;
        }
        promiseAjax.get(`/applicationconfig/${profile}/applications?searchKey=${currentValue}`).then(rsp => {
            const result = rsp.data;
            const data = [];
            result.forEach((r) => {
                data.push({
                    value: `${r.description}(${r.name})`,
                    text: `${r.description}(${r.name})`,
                });
            });
            this.setState({applicationData: data})
        })
    }

    handleChange = (value) => {
        this.setState({value});
        this.fetch(value);
        const start = value.indexOf('(');
        const stop = value.indexOf(')');
        let applicationName = value;
        //  如果没有选中后台返回结果 保存输入的数据
        if (start >= 0 && stop >= 0) {
            applicationName = value.substring(start + 1, stop)
        }
        this.handleApplicationChange(applicationName);
    }

    render() {
        const {
            gettingApplicationConfig,
            applicationConfigs,
            total,
            pageNum,
            pageSize,
        } = this.state;
        const {getFieldDecorator} = this.props.form;
        const applicationOptions = this.state.applicationData.map(d => <Option key={d.value}>{d.text}</Option>);
        const formItemLayout = {
            labelCol: {
                xs: {span: 24},
                sm: {span: 6},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 18},
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
                    <PageContent className="base-application-config">
                        <QueryBar>
                            <div id="addeditform">
                                <Form onSubmit={this.handleQuery}>
                                    <Row gutter={16}>
                                        <Col {...queryItemLayout}>
                                            <FormItem
                                                {...formItemLayout}
                                                label="环境">
                                                {getFieldDecorator('profile', {
                                                    initialValue: this.props.applicationProfileName,
                                                    onChange: this.handleProfileChange,
                                                })(
                                                    <Select
                                                        showSearch
                                                        style={{width: '100%'}}
                                                        placeholder="请选择环境"
                                                        notFoundContent="暂无数据"
                                                    >
                                                        {this.renderProfileOptions()}
                                                    </Select>
                                                )}
                                            </FormItem>
                                        </Col>
                                        <Col {...queryItemLayout}>
                                            <FormItem
                                                {...formItemLayout}
                                                label="应用系统"
                                            >
                                                {getFieldDecorator('application', {
                                                    initialValue: this.props.applicationApplicationName,
                                                })(
                                                    <Select
                                                        combobox
                                                        placeholder="请输入要搜索的系统"
                                                        notFoundContent=""
                                                        style={this.props.style}
                                                        defaultActiveFirstOption={false}
                                                        showArrow={false}
                                                        filterOption={false}
                                                        onChange={this.handleChange}
                                                    >
                                                        {applicationOptions}
                                                    </Select>
                                                )}
                                            </FormItem>
                                        </Col>
                                        <Col {...btnItemLayout} style={{textAlign: 'left', paddingLeft: 30}}>
                                            <FormItem
                                                colon={false}
                                                {...formItemLayout}
                                            >
                                                <Button type="primary" htmlType="submit"
                                                        loading={gettingApplicationConfig}
                                                        style={{marginRight: '16px'}}>查询</Button>
                                                <Button type="ghost" onClick={() => this.handleResetFields()}
                                                        style={{marginRight: '16px'}}>重置</Button>
                                                <Button type="primary" onClick={this.handleAdd}>添加</Button>
                                            </FormItem>
                                        </Col>
                                    </Row>
                                </Form>
                            </div>
                        </QueryBar>
                        <QueryResult>
                            <Table
                                loading={gettingApplicationConfig}
                                size="middle"
                                rowKey={(record) => record.modifyTime}
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

export function mapStateToProps(state) {
    return {
        ...state.frame,
        ...state.pageState.applicationConfig,
        ...state.pageState.applicationHistoryList,
    };
}

const LayoutComponent = Form.create()(ApplicationConfig);
export default connectComponent({LayoutComponent, mapStateToProps});

import React, {Component} from 'react';
import {Form, Input, Button, Table, Select, Row, Col, message} from 'antd';
import {PageContent, Operator, QueryBar, QueryResult, PaginationComponent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import {browserHistory} from 'react-router';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

const FormItem = Form.Item;
const Option = Select.Option;
export const INIT_STATE = {
    scope: 'userList',
    sync: false,
    userPageNum: '',
    userPageSize: '',
    userName: '',
    userStatus: '',
    userAdministrator: '',
};

export const PAGE_ROUTE = '/base-information/users';

@Form.create()
export class LayoutComponent extends Component {
    state = {
        pageNum: 1,
        pageSize: 10,
        gettingUsers: false,
        users: [],
        total: 0,
        visible: false,
        title: '',
        userId: '',
    };

    columns = [
        {
            title: '#',
            render: (text, record, index) => (index + 1) + ((this.state.pageNum - 1) * this.state.pageSize),
        },
        {
            title: '用户名称',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '邮箱',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: '身份',
            dataIndex: 'administrator',
            key: 'administrator',
            render: (text, record) => {
                if (record.administrator === 'YES') {
                    return (
                        <span>管理员</span>
                    );
                } else {
                    return (
                        <span>普通用户</span>
                    );
                }
            },
        },
        {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            render: (text, record) => {
                if (record.status === 'VALID') {
                    return (
                        <span>正常</span>
                    );
                } else {
                    return (
                        <span>停用</span>
                    );
                }
            },
        },

        {
            title: '操作',
            key: 'operator',
            render: (text, record) => {
                const items = [
                    {
                        label: '修改',
                        onClick: () => this.handleEdit(record),
                    },
                    {
                        label: '重置密码',
                        onClick: () => this.resetPassword(record),
                    },
                ];

                return (<Operator items={items}/>);
            },
        },
    ];

    componentWillMount() {
        // 页面渲染完成，进行一次查询
        // this.search();
        const {userPageNum, userPageSize, userName, userStatus, userAdministrator} = this.props;
        let newPageNum = (userPageNum === undefined || userPageNum === '') ? 1 : userPageNum,
            newPageSize = (userPageSize === undefined || userPageSize === '') ? 10 : userPageSize,
            newName = (userName === undefined || userName === '') ? '' : userName,
            newStatus = (userStatus === undefined || userStatus === '') ? '' : userStatus,
            newAdministrator = (userAdministrator === undefined || userAdministrator === '') ? '' : userAdministrator;

        let params = {
            name: newName,
            status: newStatus,
            administrator: newAdministrator,
            pageNum: newPageNum,
            pageSize: newPageSize,
        };
        this.setState({
            gettingUsers: true,
        });
        promiseAjax.get(`/user`, params).then(rsp => {
            if (rsp.data !== undefined) {
                this.setState({
                    pageNum: rsp.data.number + 1,
                    pageSize: rsp.data.size,
                    total: rsp.data.totalElements,
                    users: rsp.data.content,
                });
            } else {
                this.setState({
                    users: [],
                });
            }
        }).finally(() => {
            this.setState({
                gettingUsers: false,
            });
        });
    }

    search = (args) => {
        const {form: {getFieldValue}} = this.props;
        let userName = getFieldValue('name');
        let userStatus = getFieldValue('status');
        let userAdministrator = getFieldValue('administrator');
        const {pageNum, pageSize} = this.state;

        this.props.actions.setState('userList', {
            userPageNum: pageNum,
            userPageSize: pageSize,
            userName: userName,
            userStatus: userStatus,
            userAdministrator: userAdministrator,
        });

        let params = {
            name: userName,
            status: userStatus,
            administrator: userAdministrator,
            pageNum,
            pageSize,
            ...args,
        };
        this.setState({
            gettingUsers: true,
        });
        promiseAjax.get(`/user`, params).then(rsp => {
            if (rsp.data !== undefined) {
                this.setState({
                    pageNum: rsp.data.number + 1,
                    pageSize: rsp.data.size,
                    total: rsp.data.totalElements,
                    users: rsp.data.content,
                });
            } else {
                this.setState({
                    users: [],
                });
            }
        }).finally(() => {
            this.setState({
                gettingUsers: false,
            });
        });
    };

    /**
     *  添加
     */
    handleAdd = () => {
        browserHistory.push('/base-information/users/+add/addId');
    };

    handleSave = () => {
        this.search();
    };

    /**
     * 修改
     * @param record
     */
    handleEdit = (record) => {
        browserHistory.push(`${'/base-information/users/+add/:addId'.replace(':addId', record.id)}`);
    };

    /**
     * 密码重置
     * @param record
     */
    resetPassword = (record) => {
        promiseAjax.put(`/user/${record.id}/reset_password?password=123456`).then((res) => {
            if (res.status === 'SUCCESS') {
                message.success('密码重置成功', 3);
            }
        })
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

    /**
     * 重置搜索表单
     */
    handleResetFields() {
        this.props.actions.setState('userList', {
            userPageNum: 1,
            userPageSize: 10,
            userName: '',
            userStatus: '',
            userAdministrator: '',
        });
        this.props.form.resetFields();
    }

    render() {
        const {
            gettingUsers,
            users,
            total,
            pageNum,
            pageSize,
        } = this.state;
        const {form: {getFieldDecorator}} = this.props;
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
            xs: 12,
            md: 5,
            lg: 5,
        };
        const btnItemLayout = {
            xs: 12,
            md: 9,
            lg: 9,
        };
        return (
            <div>
                <PageContent className="base-user">
                    <QueryBar>
                        <Form onSubmit={this.handleQuery}>
                            <Row gutter={16}>
                                <Col {...queryItemLayout}>
                                    <FormItem
                                        {...formItemLayout}
                                        label="姓名">
                                        {getFieldDecorator('name', {
                                            initialValue: this.props.userName,
                                        })(
                                            <Input placeholder="请输入登录名称" style={{width: '100%'}}/>
                                        )}
                                    </FormItem>
                                </Col>
                                <Col {...queryItemLayout}>
                                    <FormItem
                                        {...formItemLayout}
                                        label="状态">
                                        {getFieldDecorator('status', {
                                            initialValue: this.props.userStatus,
                                        })(
                                            <Select
                                                placeholder="状态"
                                            >
                                                <Option value="VALID">正常</Option>
                                                <Option value="INVALID">停用</Option>
                                            </Select>
                                        )}
                                    </FormItem>
                                </Col>
                                <Col {...queryItemLayout}>
                                    <FormItem
                                        {...formItemLayout}
                                        label="角色">
                                        {getFieldDecorator('administrator', {
                                            initialValue: this.props.userAdministrator,
                                        })(
                                            <Select
                                                placeholder="请选择角色"
                                            >
                                                <Option value="YES">管理员</Option>
                                                <Option value="NO">普通用户</Option>
                                            </Select>
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
                                            loading={gettingUsers}
                                            style={{marginRight: '16px'}}
                                        >查询</Button>

                                        <Button
                                            type="ghost"
                                            style={{marginRight: '16px'}}
                                            onClick={() => this.handleResetFields()}
                                        >重置</Button>

                                        <Button type="primary" onClick={this.handleAdd}>添加</Button>
                                    </FormItem>
                                </Col>
                            </Row>
                        </Form>
                    </QueryBar>
                    <QueryResult>
                        <Table
                            loading={gettingUsers}
                            size="middle"
                            rowKey={(record) => record.id}
                            columns={this.columns}
                            dataSource={users}
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

            </div>
        );
    }
}

export function mapStateToProps(state) {
    return {
        ...state.frame,
        ...state.pageState.instance,
        ...state.pageState.userList,
    };
}

export default connectComponent({LayoutComponent, mapStateToProps});
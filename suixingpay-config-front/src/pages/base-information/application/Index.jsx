import React, {Component} from 'react';
import {Form, Input, Button, Table, Row, Col, Modal, message} from 'antd';
import {PageContent, Operator, QueryBar, QueryResult, ToolBar, PaginationComponent, Permission} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import './style.less';
import ValidationRule from '../../../commons/validation-rule';

const FormItem = Form.Item;

export const PAGE_ROUTE = '/base-information/application';

class ApplicationList extends Component {
    state = {
        pageNum: 1,
        pageSize: 10,
        applications: [],
        total: 0,
        queryLoading: false,
        visible: false,
        title: '',
        isEdit: false,
        editApplications: [],
    };

    columns = [
        {
            title: '#',
            render: (text, record, index) => (index + 1) + ((this.state.pageNum - 1) * this.state.pageSize),
        },
        {
            title: '应用名称',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '显示名称',
            dataIndex: 'description',
            key: 'description',
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
            queryLoading: true,
        });
        promiseAjax.get(`/application`, params).then(rsp => {
            if (rsp.data != null) {
                this.setState({
                    pageNum: rsp.data.number + 1,
                    pageSize: rsp.data.size,
                    total: rsp.data.totalElements,
                    applications: rsp.data.content,
                });
            } else {
                this.setState({
                    pageNum: 1,
                    total: 0,
                    applications: [],
                });
            }
        }).finally(() => {
            this.setState({
                queryLoading: false,
            });
        });
    };

    /**
     *  添加
     */
    handleAdd = () => {
        this.props.form.resetFields();
        this.showModal();
    };

    /**
     * 点击修改
     * @param record
     */
    handleEdit = (record) => {
        const {setFieldsValue} = this.props.form;
        setFieldsValue({addEditName: record.name});
        setFieldsValue({addEditDescription: record.description});
        this.setState({
            visible: true,
            isEdit: true,
            title: '应用信息修改',
            editApplications: record,
        });
    };

    /**
     * 显示添加页面
     */
    showModal = () => {
        this.setState({
            visible: true,
            isEdit: false,
            title: '新增应用',
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

    componentDidMount() {
        // 页面渲染完成，进行一次查询
        this.search();
    }

    handleOk = () => {
        this.setState({
            visible: false,
        });
    };

    handleCancel = () => {
        this.setState({
            visible: false,
        });
    };

    handleSubmit = (e) => {
        e.preventDefault();
        const {form} = this.props;
        form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                let params = {
                    name: values.addEditName,
                    description: values.addEditDescription,
                };
                if (!this.state.isEdit) {
                    params.password = values.password;
                    promiseAjax.post('/application', params).then((res) => {
                        if (res.status === 'SUCCESS') {
                            message.success('添加成功', 3);
                        } else {
                            message.error('添加失败', 3);
                        }
                    }).finally(() => {
                        this.setState({visible: false});
                        this.props.form.resetFields();
                        this.search();
                    });
                } else {
                    promiseAjax.put('/application', params).then((res) => {
                        if (res.status === 'SUCCESS') {
                            message.success('修改成功', 3);
                        } else {
                            message.error('修改失败', 3);
                        }
                    }).finally(() => {
                        this.setState({
                            visible: false,
                        });
                        this.props.form.resetFields();
                        this.search();
                    });
                }

            }
        });
    };

    /**
     * 检查唯一性  是否可用
     * @param ignoreValues
     * @returns {*}
     */
    checkKeyUnique(ignoreValues = []) {
        if (typeof ignoreValues === 'string') {
            ignoreValues = [ignoreValues];
        }
        return {
            validator(rule, value, callback) {
                if (!value || ignoreValues.indexOf(value) > -1) {
                    return callback();
                }
                promiseAjax
                    .get(`/application/checkKeyUnique/${value}`)
                    .then(res => {
                        if (res.data) {
                            callback('抱歉，该数据已经存在！');
                        }
                        callback();
                    });
            },
        };
    }

    render() {
        const {
            applications,
            total,
            pageNum,
            pageSize,
            queryLoading,
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
            xs: 12,
            md: 5,
            lg: 5,
        };
        const btnItemLayout = {
            xs: 12,
            md: 14,
            lg: 14,
        };
        return (
            <div>
                <PageContent>
                    <QueryBar>
                        <Form onSubmit={this.handleQuery}>
                            <Row gutter={16}>
                                <Col {...queryItemLayout}>
                                    <FormItem
                                        {...formItemLayout}
                                        label="应用名称">
                                        {getFieldDecorator('searchKey')(
                                            <Input placeholder="请输入应用名称" style={{width: '100%'}}/>
                                        )}
                                    </FormItem>
                                </Col>
                                <Col {...btnItemLayout} style={{textAlign: 'left', paddingLeft: 30}}>
                                    <FormItem
                                        colon={false}
                                        {...formItemLayout}
                                    >
                                        <Button type="primary" htmlType="submit" loading={queryLoading}
                                                style={{marginRight: '16px'}}>查询</Button>
                                        <Button type="ghost" onClick={() => this.props.form.resetFields()}
                                                style={{marginRight: '16px'}}>重置</Button>
                                        <Button type="primary" onClick={this.handleAdd}>添加</Button>
                                    </FormItem>
                                </Col>
                            </Row>
                        </Form>
                    </QueryBar>
                    <ToolBar>
                        <Modal footer={null} title={this.state.title} visible={this.state.visible} onOk={this.handleOk}
                               onCancel={this.handleCancel}
                        >
                            <Form onSubmit={this.handleSubmit}>
                                <FormItem
                                    {...formItemLayout}
                                    label="应用名称"
                                >
                                    {getFieldDecorator('addEditName', {
                                        initialValue: this.state.isEdit ? this.state.editApplications.name : '',
                                        validateTrigger: 'onBlur',
                                        rules: [
                                            {
                                                required: true, message: '请输入应用名称!',
                                            },
                                            ValidationRule.checkLength('应用名称最大长度为40'),
                                            this.state.isEdit ? '' : this.checkKeyUnique(),
                                        ],
                                    })(
                                        <Input placeholder="请输入应用名称" disabled={this.state.isEdit}/>
                                    )}
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label="显示名称"
                                >
                                    {getFieldDecorator('addEditDescription', {
                                        initialValue: this.state.isEdit ? this.state.editApplications.description : '',
                                        rules: [
                                            {
                                                required: true, message: '请输入显示名称！',
                                            },
                                            ValidationRule.checkLength('显示名称最大长度为40'),
                                        ],
                                    })(
                                        <Input placeholder="请输入显示名称"/>
                                    )}
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label=" "
                                    colon={false}
                                >
                                    <Button type="primary" htmlType="submit" size="large"
                                            style={{marginRight: 16}}>保存</Button>
                                    <Button
                                        type="ghost" htmlType="reset" size="large"
                                        onClick={() => this.props.form.resetFields()}>
                                        重置
                                    </Button>
                                </FormItem>
                            </Form>
                        </Modal>
                    </ToolBar>
                    <QueryResult>
                        <Table
                            loading={queryLoading}
                            size="middle"
                            rowKey={(record) => record.name}
                            columns={this.columns}
                            dataSource={applications}
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

export default Form.create()(ApplicationList);

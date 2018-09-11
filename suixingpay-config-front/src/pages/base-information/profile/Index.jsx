import React, {Component} from 'react';
import {Form, Input, Button, Table, Popconfirm, Modal, message} from 'antd';
import {PageContent, Operator, QueryResult, ToolBar} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import ValidationRule from '../../../commons/validation-rule';
import './style.less';

const FormItem = Form.Item;

export const PAGE_ROUTE = '/base-information/profile';

class ProFileList extends Component {
    state = {
        profiles: [],
        queryLoading: false,
        visible: false,
        isEdit: false,
        title: '',
        editProfiles: [],
    };

    columns = [
        {
            title: '#',
            render: (text, record, index) => (index + 1),
        },
        {
            title: '环境名称',
            dataIndex: 'profile',
            key: 'profile',
        },
        {
            title: '显示名称',
            dataIndex: 'name',
            key: 'name',
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

    search = () => {
        const {form: {getFieldsValue}} = this.props;
        let params = {
            ...getFieldsValue(),
        };
        this.setState({
            queryLoading: true,
        });
        promiseAjax.get('/profile', params).then(rsp => {
            this.setState({
                profiles: rsp.data,
            });
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
        setFieldsValue({addEditProfile: record.profile});
        this.setState({
            visible: true,
            isEdit: true,
            title: '环境信息修改',
            editProfiles: record,
        });
    };

    /**
     * 显示添加页面
     */
    showModal = () => {
        this.setState({
            visible: true,
            isEdit: false,
            title: '新增环境',
        });
    };

    /**
     * 查询
     * @param e
     */
    handleQuery = (e) => {
        e.preventDefault();
        this.search();
    };

    componentWillMount() {
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
                    profile: values.addEditProfile,
                };

                if (!this.state.isEdit) {
                    params.password = values.password;
                    promiseAjax.post('/profile', params).then((res) => {
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
                    promiseAjax.put('/profile', params).then((res) => {
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
                    .get(`/profile/checkKeyUnique/${value}`)
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
            profiles,
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
        return (
            <div>
                <PageContent className="base-profile">
                    <ToolBar>
                        <div style={{textAlign: 'right'}}>
                            <Button type="primary" onClick={this.handleAdd}>添加</Button>
                        </div>
                        <Modal footer={null} title={this.state.title} visible={this.state.visible} onOk={this.handleOk} onCancel={this.handleCancel}
                        >
                            <Form onSubmit={this.handleSubmit}>
                                <FormItem
                                    {...formItemLayout}
                                    label="环境名称"
                                >
                                    {getFieldDecorator('addEditProfile', {
                                        initialValue: this.state.isEdit ? this.state.editProfiles.profile : '',
                                        validateTrigger: 'onBlur',
                                        rules: [
                                            {
                                                required: true, message: '请输入环境名称!',
                                            },
                                            ValidationRule.checkLength('环境名称最大长度为40'),
                                            this.state.isEdit ? '' : this.checkKeyUnique(),
                                        ],
                                    })(
                                        <Input placeholder="请输入环境名称" disabled={this.state.isEdit}/>
                                    )}
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label="显示名称"
                                >
                                    {getFieldDecorator('addEditName', {
                                        initialValue: this.state.isEdit ? this.state.editProfiles.name : '',
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
                                    <Button type="primary" htmlType="submit" size="large" style={{marginRight: 16}}>保存</Button>
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
                            rowKey={(record) => record.profile}
                            columns={this.columns}
                            dataSource={profiles}
                            pagination={false}
                        />
                    </QueryResult>
                </PageContent>
            </div>
        );
    }
}

export default Form.create()(ProFileList);

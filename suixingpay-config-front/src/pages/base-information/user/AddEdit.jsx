import React, {Component} from 'react';
import {Form, Input, Button, Row, Col, Switch, Table, Checkbox, Popconfirm, Select, message} from 'antd';
import {PageContent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import ValidationRule from '../../../commons/validation-rule';
import Utils from '../../../commons/utils';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

const Option = Select.Option;
const FormItem = Form.Item;

export const PAGE_ROUTE = '/base-information/users/+add/:userId';

class UserList extends Component {
    state = {
        title: '新增用户',
        isEdit: false,
        userId: null,
        gettingUser: false,
        user: {},
        gettingApplication: false,
        saveAndUpdataLoading: false,
        applications: [],
        dataApplications: [],
        profiles: [],
        checkProfiles: [],
        activeApplicationTabKey: null,
        dataSource: [],
        count: '',
        isEditProfile: true,
    };

    columns = [
        {
            title: '环境',
            dataIndex: 'profile',
            width: '20%',
        },
        {
            title: '应用系统',
            dataIndex: 'application',
        },
        {
            title: '操作',
            render: (text, record) => {
                return (
                    <Popconfirm title="Sure to delete?" onConfirm={() => this.onDelete(record.key)}>
                        <a href="#">Delete</a>
                    </Popconfirm>
                );
            },
        }
    ];

    componentWillMount() {
        const {params: {userId}} = this.props;
        if (userId !== 'addId') {
            this.setState({
                title: '修改用户',
                userId,
                isEdit: true,
                gettingUser: true,
                isEditProfile: true,
            });
            promiseAjax.get(`/user/${userId}`).then(rsp => {
                if (rsp) {
                    this.setState({
                        user: rsp.data,
                    });
                }
            }).finally(() => {
                this.setState({
                    gettingUser: false,
                });
            });
            promiseAjax.get(`/userapplicationconfigrole/${userId}`).then(rsp => {
                let dataSource = [];
                rsp.data.map(item => {
                    let newData = {
                        key: `${item.application.name},${item.profile.name}`,
                        application: item.application.description + '(' + item.application.name + ')',
                        profile: item.profile.name + '(' + item.profile.profile + ')',
                        applicationName: item.application.name,
                        profileId: item.profile.profile,
                    };
                    dataSource.push(newData);
                });

                this.setState({dataSource});
            });

            promiseAjax.get(`/userglobalconfigrole/${userId}`).then(rsp => {
                let checkProfiles = [];
                rsp.data.map(item => {
                    checkProfiles.push(item.profile);
                });

                this.setState({checkProfiles});
            })
        }
        promiseAjax.get('globalconfig/profiles').then(rsp => {
            this.setState({
                profiles: rsp.data,
            })
        })

    }

    /**
     * 提交表单
     */
    handleSubmit = () => {
        const {form} = this.props;
        form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                if (values.status !== undefined && values.status) {
                    values.status = 'VALID'
                } else {
                    values.status = 'INVALID'
                }
                if (values.administrator !== undefined && values.administrator) {
                    values.administrator = 'YES'
                } else {
                    values.administrator = 'NO'
                }

                const {dataSource, checkProfiles} = this.state;
                let applicationProfileRoles = [];
                let profileRolesTmp = [];
                let profileRoles = [];

                dataSource.map(item => {
                    applicationProfileRoles.push({
                        application: {
                            name: item.applicationName,
                        },
                        profile: {
                            profile: item.profileId,
                        },
                    });
                    profileRolesTmp.push(item.profile);
                });

                // 应用环境权限
                values.applicationProfileRoles = applicationProfileRoles;
                // 去重后填充数据
                Utils.getUniqueArray(profileRolesTmp).map(item => {
                    profileRoles.push({
                        profile: item
                    })
                });
                // 全局权限
                values.profileRoles = checkProfiles;
                if (values.id === '' || values.id === undefined) {
                    promiseAjax.post('/user', values).then((res) => {
                        if (res.status === 'SUCCESS') {
                            message.success('添加成功', 3);
                            history.back();
                        } else {
                            message.error('添加失败', 3);
                        }
                    }).finally(() => {
                        // this.props.handleSave(false);

                    });
                } else {
                    promiseAjax.put('/user', values).then((res) => {
                        if (res.status === 'SUCCESS') {
                            message.success('修改成功', 3);
                            history.back();
                        } else {
                            message.error('修改失败', 3);
                        }
                    }).finally(() => {
                        // this.props.handleSave(false);
                    });
                }
            }
        });
    };


    /**
     * 重置表单数据
     * 直接食用restFields不会重置checkbox
     */
    resetFieldsForm = () => {
        this.props.form.resetFields();
        this.setState({
            profileNames: [],
            applicationNames: [],
            isEditProfile: true,
        });
    }

    fetch(value) {
        const currentValue = value;
        let profile = this.state.profileName;
        promiseAjax.get(`/applicationconfig/${profile}/applications?searchKey=${currentValue}`).then(rsp => {
            if (rsp.status === 'SUCCESS') {
                if (rsp.data.length === 0) {
                    this.props.form.setFields({
                        application: {
                            value: value,
                            errors: [new Error('此系统不存在！')],
                        },
                    });
                } else {
                    const result = rsp.data;
                    const data = [];
                    result.forEach((r) => {
                        data.push({
                            value: `${r.description}(${r.name})`,
                            text: `${r.description}(${r.name})`,
                        });
                    });
                    if (result.length > 0) {
                        this.setState({
                            applications: data,
                            dataApplications: result,
                        })
                    }
                }
            }
        })
    }

    handleChange = (value) => {
        this.setState({value});
        this.fetch(value);
        const start = value.indexOf('(');
        const stop = value.indexOf(')');
        this.setState({
            applicationName: value.substring(start + 1, stop),
        })
    }

    /**
     * 选择环境
     * @param profileName
     */
    handleProfileChange = (profileName) => {
        if (profileName !== undefined && profileName !== '') {
            this.setState({
                isEditProfile: false,
            });
        }
        this.setState({
            profileName,
        });
    };

    /**
     * 初始化环境下拉菜单
     */
    renderProfileOptions = () => {
        return this.state.profiles.map(item => <Option key={item.profile}>{item.name}({item.profile})</Option>);
    };

    /**
     * 删除权限
     * @param key
     */
    onDelete = (key) => {
        const dataSource = [...this.state.dataSource];
        this.setState({dataSource: dataSource.filter(item => item.key !== key)});
    };

    /**
     * 添加权限
     */
    handleAdd = () => {
        const {setFieldsValue} = this.props.form;
        const {applicationName, dataApplications, profileName, profiles, count, dataSource} = this.state;
        const dataSource0 = [...this.state.dataSource];
        if (!this.checkRole(applicationName, profileName)) {
            return false;
        }
        const application = dataApplications.find(it => it.name === applicationName);
        const profile = profiles.find(it => it.profile === profileName);
        if (application === undefined || application === '') {
            return false;
        }
        const datakey = application.name + ',' + profile.name;

        if (dataSource0.find(item => item.key === datakey) !== undefined) {
            this.props.form.setFields({
                profile: {
                    value: profile.profile,
                    errors: [new Error('此记录已存在！')],
                },
            });
            return false;
        }
        const newData = {
            key: `${application.name},${profile.name}`,
            application: application.description + '(' + application.name + ')',
            profile: profile.name + '(' + profile.profile + ')',
            applicationName: application.name,
            profileId: profile.profile,
        };

        this.setState({
            dataSource: [...dataSource, newData],
            count: count + 1,
            applicationName: '',
            //profileName: '',
        });
        setFieldsValue({application: ''});
        //setFieldsValue({profile: profileName});
    };


    /**
     * 添加角色校验
     * @param applicationName
     * @param profile
     * @returns {boolean}
     */
    checkRole(applicationName, profile) {
        if (profile === undefined || profile === '') {
            this.props.form.setFields({
                profile: {
                    errors: [new Error('请选择环境！')],
                },
            });
            return false;
        }

        if (applicationName === undefined || applicationName === '') {
            this.props.form.setFields({
                application: {
                    errors: [new Error('请选择应用系统！')],
                },
            });
            return false;
        }
        return true;
    }

    /**
     * 全局单选框
     */
    renderDetailTabPanes = () => {
        const {getFieldDecorator} = this.props.form;
        const {profiles, checkProfiles} = this.state;
        const roleLayout = {
            xs: 8,
            sm: 6,
            md: 4,
            lg: 3,
            xl: 2,
        };
        const {params: {userId}} = this.props;
        if (userId !== 'addId') {
            return (
                <Row>
                    {profiles.map(temp => {
                        const checked = checkProfiles.find(checkProfile => checkProfile.profile === temp.profile) !== undefined;
                        return (
                            <Col {...roleLayout} key={temp.profile}>
                                <Checkbox
                                    checked={checked}
                                    onChange={e => this.handleRoleCheck(e.target.checked, temp)}
                                >
                                    {temp.name + '(' + temp.profile + ')'}
                                </Checkbox>
                            </Col>
                        );
                    })}
                </Row>
            );
        }
        if (userId === 'addId') {
            return (
                <Row>
                    {profiles.map(temp => {
                        return (
                            <Col {...roleLayout} key={temp.profile}>
                                {getFieldDecorator(`${temp.name}`, {
                                    valuePropName: 'checked',
                                })(
                                    <Checkbox
                                        onChange={e => this.handleRoleCheck(e.target.checked, temp)}
                                    >
                                        {temp.name + '(' + temp.profile + ')'}
                                    </Checkbox>
                                )}

                            </Col>
                        );
                    })}
                </Row>
            );
        }
    }

    /**
     * 单选环境，存入state
     * @param checked
     * @param profile
     */
    handleRoleCheck = (checked, profile) => {
        let {checkProfiles} = this.state;
        if (checked) {
            checkProfiles.push(profile);
        } else {
            Utils.arrayRemove(checkProfiles, profile);
        }
        this.setState({checkProfiles});
    };

    /**
     * 检查主键唯一性  是否可用
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
                    .get(`/user/checkNameUnique/${value}`)
                    .then(res => {
                        if (res.data) {
                            callback('抱歉，该登录名称已被占用！');
                        }
                        callback();
                    });
            },
        };
    }

    componentDidMount() {
        document.getElementById('addeditform').getElementsByTagName('form')[0].setAttribute('autocomplete', 'off');
    }

    render() {
        const {getFieldDecorator} = this.props.form;
        const {user, isEdit, saveAndUpdataLoading} = this.state;
        const options = this.state.applications.map(d => <Option key={d.value}>{d.text}</Option>);
        const formItemLayout = {
            labelCol: {
                xs: {span: 24},
                sm: {span: 6},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 14},
            },
        };
        const colItemLayout = {
            xs: {span: 24},
            md: {span: 10},
            lg: {span: 8},
        };
        const {dataSource} = this.state;
        const columns = this.columns;

        return (
            <PageContent className="base-business-user-add">
                <div id="addeditform">
                    <Form onSubmit={this.handleSubmit}>
                        <div className="sub-title">账号信息</div>
                        <Row>
                            <Col {...colItemLayout}>
                                <FormItem
                                    {...formItemLayout}
                                    label="登录名称"
                                >
                                    {getFieldDecorator('name', {
                                        validateTrigger: 'onBlur',
                                        initialValue: user.name,
                                        rules: [
                                            {
                                                required: true, message: '请输入登陆名称!',
                                            },
                                            this.state.isEdit ? '' : this.checkKeyUnique(),

                                        ],
                                    })(
                                        <Input disabled={this.state.isEdit}/>
                                    )}
                                </FormItem>
                                {isEdit ? null : (
                                    <FormItem
                                        {...formItemLayout}
                                        label="密码"
                                    >
                                        {getFieldDecorator('password', {
                                            rules: [
                                                {
                                                    required: true, message: '请输入密码!',
                                                },
                                                ValidationRule.passWord('密码长度为6-40位'),
                                            ],
                                        })(
                                            <Input/>
                                        )}
                                    </FormItem>
                                )}
                                <FormItem
                                    {...formItemLayout}
                                    label="邮箱"
                                >
                                    {getFieldDecorator('email', {
                                        initialValue: user.email,
                                        rules: [
                                            {required: true, message: '请输入邮箱！',},
                                            ValidationRule.email('请正确输入邮箱'),
                                            ValidationRule.checkLength('邮箱最大长度为40'),
                                        ],
                                    })(
                                        <Input/>
                                    )}
                                </FormItem>
                                {
                                    isEdit ?
                                        <FormItem
                                            {...formItemLayout}
                                            label=""
                                            style={{marginBottom: 0}}
                                        >
                                            {getFieldDecorator('id', {
                                                initialValue: user.id,
                                            })(
                                                <Input type="hidden"/>
                                            )}
                                        </FormItem>
                                        : null
                                }

                                <FormItem
                                    {...formItemLayout}
                                    label="是否为管理员"
                                >
                                    {getFieldDecorator('administrator', {
                                        initialValue: user.administrator === undefined ? false : user.administrator === 'YES',
                                        valuePropName: 'checked',
                                        rules: [
                                            {
                                                required: false, message: '请选择是否为管理员!',
                                            },
                                        ],
                                    })(
                                        <Switch checkedChildren={'是'} unCheckedChildren={'否'}/>
                                    )}
                                </FormItem>
                                {isEdit ?
                                    <FormItem
                                        {...formItemLayout}
                                        label="账号是否可用"
                                    >
                                        {getFieldDecorator('status', {
                                            initialValue: user.status === undefined ? true : user.status === 'VALID',
                                            valuePropName: 'checked',
                                            rules: [
                                                {
                                                    required: false, message: '请选择账号是否可用!',
                                                },
                                            ],
                                        })(
                                            <Switch checkedChildren={'是'} unCheckedChildren={'否'}/>
                                        )}
                                    </FormItem>
                                    :
                                    null
                                }

                            </Col>
                            <Col></Col>
                        </Row>

                        <div className="sub-title">应用权限</div>
                        <Row>
                            <Col {...colItemLayout}>
                                <FormItem
                                    {...formItemLayout}
                                    label="环境">
                                    {getFieldDecorator('profile', {
                                        onChange: this.handleProfileChange,
                                        rules: [{required: false, message: '请选择状态'}],
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
                            <Col {...colItemLayout}>
                                <FormItem
                                    {...formItemLayout}
                                    label="应用系统">
                                    {getFieldDecorator('application', {
                                        rules: [{required: false, message: '请选择状态'}],
                                    })(
                                        <Select combobox
                                                placeholder="请输入要搜索的系统"
                                                style={this.props.style}
                                                defaultActiveFirstOption={false}
                                                showArrow={false}
                                                filterOption={false}
                                                disabled={this.state.isEditProfile}
                                                onChange={this.handleChange}
                                        >
                                            {options}
                                        </Select>
                                    )}
                                </FormItem>
                            </Col>
                            <Col>
                                <Button className="editable-add-btn" onClick={this.handleAdd}>添加</Button>
                            </Col>
                        </Row>
                        <div style={{marginLeft: 110}}>
                            <Table size="middle" pagination={false} dataSource={dataSource} columns={columns}
                                   style={{width: '69%'}}/>
                        </div>

                        <div style={{marginTop: 20}}>
                            <div className="sub-title">环境权限</div>
                            <div style={{marginLeft: 110}}>
                                {this.renderDetailTabPanes()}
                            </div>
                        </div>
                        <div style={{marginTop: 20}}>
                            <Button
                                type="primary"
                                size="large"
                                loading={saveAndUpdataLoading}
                                style={{marginRight: 16}}
                                onClick={() => this.handleSubmit()}
                            >保存</Button>

                            <Button
                                type="ghost"
                                htmlType="reset"
                                size="large"
                                onClick={this.resetFieldsForm} style={{marginRight: 16}}
                            >重置</Button>

                            <Button type="primary" size="large" onClick={() => history.back()}>返回</Button>
                        </div>
                    </Form>
                </div>
            </PageContent>
        );
    }
}

function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

export const LayoutComponent = Form.create()(UserList);
export default connectComponent({LayoutComponent, mapStateToProps});

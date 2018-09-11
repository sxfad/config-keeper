import React, {Component, PropTypes} from 'react';
import {Form, Input, Button, Modal} from 'antd';
import {promiseAjax} from 'sx-ui';
import './style.less';
import {logout} from '../../../commons/index';
import ValidationRule from '../../../commons/validation-rule';
import connectComponent from '../../../redux/store/connectComponent';

const createForm = Form.create;
const FormItem = Form.Item;

function noop() {
    return false;
}


@createForm()
class LayoutComponent extends Component {
    state = {
        visible: false,
        title: '修改密码',
    };

    static defaultProps = {
        loading: false,
    };

    static propTypes = {
        loading: PropTypes.bool,
    };

    componentWillMount() {
        this.setState({
            visible: this.props.clickPassword,
        });
    }


    handleReset = (e) => {
        e.preventDefault();
        this.props.form.resetFields();
    }

    handleSubmit = (e) => {
        const {loading, form} = this.props;

        if (loading) {
            return;
        }

        e.preventDefault();
        form.validateFields((errors, values) => {
            if (errors) {
                return false;
            }
            promiseAjax.put(`/user/change_self_password/?new_password=${values.new_password}&old_password=${values.old_password}`).then((rsp) => {
                if (rsp.status === 'SUCCESS') {
                    setTimeout(() => {
                        logout();
                    }, 1000);
                }
            });
        });
    }

    checkPass = (rule, value, callback) => {
        const {validateFields} = this.props.form;

        if (value) {
            validateFields(['rePass'], {force: true});
        }
        callback();
    }

    checkRePass = (rule, value, callback) => {
        const {getFieldValue} = this.props.form;

        if (value && value !== getFieldValue('new_password')) {
            callback('两次输入密码不一致！');
        } else {
            callback();
        }
    }
    handleOk = () => {
        this.setState({
            visible: false,
        });
    }
    handleCancel = () => {
        this.setState({
            visible: false,
        });
        this.props.handleCancel(false);
    }

    render() {
        const {loading, form: {getFieldDecorator}} = this.props;
        const orPasswdDecorator = getFieldDecorator('old_password', {
            rules: [
                {required: true, whitespace: true, message: '请填写原密码'},
            ],
        });
        const newPassDecorator = getFieldDecorator('new_password', {
            rules: [
                {required: true, whitespace: true, message: '请填写密码'},
                {validator: this.checkPass},
                ValidationRule.passWord('请输入正确的密码，密码长度6-40！'),

            ],
        });
        const newPassRepeatDecorator = getFieldDecorator('rePass', {
            rules: [
                {
                    required: true,
                    whitespace: true,
                    message: '请再次输入密码',

                },
                {
                    validator: this.checkRePass,
                },
                // ValidationRule.passWord('请输入正确的密码，密码同时包含大小写字母、数字、字符，且长度6-40！'),
            ],
        });
        const formItemLayout = {
            labelCol: {span: 7},
            wrapperCol: {span: 12},
        };
        return (
            <Modal footer={null} title={this.state.title} visible={this.state.visible} onOk={this.handleOk} onCancel={this.handleCancel}
            >
                <Form layout="horizontal" onSubmit={this.handleSubmit} onReset={this.handleReset}>
                    <FormItem
                        {...formItemLayout}
                        label="原密码："
                    >
                        {orPasswdDecorator(
                            <Input
                                type="password"
                                autoComplete="off"
                                onContextMenu={noop}
                                onPaste={noop}
                                onCopy={noop}
                                onCut={noop}
                            />
                        )}
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="新密码："
                    >
                        {newPassDecorator(
                            <Input
                                type="password"
                                autoComplete="off"
                                onContextMenu={noop}
                                onPaste={noop}
                                onCopy={noop}
                                onCut={noop}
                            />
                        )}
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="确认密码："
                    >
                        {newPassRepeatDecorator(
                            <Input
                                type="password"
                                autoComplete="off"
                                onContextMenu={noop}
                                onPaste={noop}
                                onCopy={noop}
                                onCut={noop}
                            />
                        )}
                    </FormItem>
                    <FormItem wrapperCol={{span: 12, offset: 7}}>
                        <Button type="ghost" style={{marginRight: 8}} htmlType="reset">重置</Button>
                        <Button type="primary" loading={loading} htmlType="submit">确定</Button>
                    </FormItem>
                </Form>
            </Modal>
        );
    }
}


export function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

export default connectComponent({LayoutComponent, mapStateToProps});

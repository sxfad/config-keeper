import React, {Component} from 'react';
import {Form, Input, Button, Modal, Row, Col, Select, message} from 'antd';
import {PageContent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import jsyaml from 'js-yaml';
import codemirror from 'codemirror';
import 'codemirror/mode/yaml/yaml.js';
import 'codemirror/mode/javascript/javascript.js';
import util from 'util';
import base64 from '../base64';
import './style.less';
import connectComponent from '../../../redux/store/connectComponent';
import DiffComponent from './DiffComponent';

const Option = Select.Option;
const FormItem = Form.Item;
const inspect = util.inspect;

let source,
    result,
    permalink,
    default_text;

const SexyYamlType = new jsyaml.Type('!sexy', {
    kind: 'sequence',
    construct: function (data) {
        return data.map(function (string) {
            return 'sexy ' + string;
        });
    }
});
const SEXY_SCHEMA = jsyaml.Schema.create([SexyYamlType]);

export const PAGE_ROUTE = '/base-information/application-config/+add/:profile/:application';

class ApplicationConfigAddEdit extends Component {
    state = {
        title: '新增配置',
        isEdit: false,
        applicationConfig: [],
        saveAndUpdataLoading: false,
        application: [],
        applicationNames: [],
        profile: [],
        profileNames: [],
        applicationName: '',
        isEditProfile: true,
        visible: false,
        editConfigText: '',
    };

    componentDidMount() {
        document.getElementById('addeditform').getElementsByTagName('form')[0].setAttribute('autocomplete', 'off');
        const {params: {profile, application}} = this.props;
        promiseAjax.get('/applicationconfig/profiles').then(rsp => {
            this.setState({
                profile: rsp.data,
            })
        });


        if (application !== 'addApplication' && profile !== 'addProfile') {
            this.setState({
                title: '修改配置',
                isEdit: true,
                isEditProfile: true,
            });
            promiseAjax.get(`/applicationconfig/${application}/${profile}`).then(rsp => {
                console.log(rsp.data);
                if (rsp) {
                    this.setState({
                        applicationConfig: rsp.data,
                    }, () => {
                        this.initContentConfig();
                    });
                }
            })
        } else {
            this.initContentConfig();
        }

    }

    initContentConfig = () => {
        const {setFieldsValue} = this.props.form;
        const permalink = document.getElementById('permalink');
        const default_text = document.getElementById('propertySource').value || '';
        const source = codemirror.fromTextArea(document.getElementById('propertySource'), {
            mode: 'yaml',
            lineNumbers: true
        });

        let timer;
        source.on('change', function () {
            clearTimeout(timer);
            timer = setTimeout(function () {
                let str, obj;
                str = source.getValue();
                setFieldsValue({
                    propertySource: str,
                });

                permalink.href = '#yaml=' + base64.encode(str);
                try {
                    obj = jsyaml.load(str, {schema: SEXY_SCHEMA});

                    result.setOption('mode', 'javascript');
                    result.setValue(inspect(obj, false, 10));
                } catch (err) {
                    result.setOption('mode', 'text/plain');
                    result.setValue(err.message || String(err));
                }
            }, 500);
        });
        result = codemirror.fromTextArea(document.getElementById('result'), {
            readOnly: true
        });

        // initial source
        let yaml;
        if (location.hash && location.hash.toString().slice(0, 6) === '#yaml=') {
            yaml = base64.decode(location.hash.slice(6));
        }
        source.setValue(yaml || default_text);
    };

    /**
     * 提交
     */
    handleSubmit = () => {
        const {form} = this.props;
        form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                values.application = this.subStr(values.application);
                const {isEdit} = this.state;

                if (isEdit) {
                    this.setState({
                        visible: true,
                        editConfigText: values.propertySource,
                    });
                    // promiseAjax.put('/applicationconfig', values).then(rsp => {
                    //     if (rsp.status === 'SUCCESS') {
                    //         message.success('修改成功', 3);
                    //         history.back();
                    //     }
                    // });
                } else {
                    promiseAjax.post('/applicationconfig', values).then(rsp => {
                        if (rsp.status === 'SUCCESS') {
                            message.success('添加成功', 3);
                            history.back();
                        }
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
    };

    fetch(value) {
        const currentValue = value;
        let profileName = 'undefined';
        const {params: {profile}} = this.props;

        if (profile !== undefined && profile !== '') {
            profileName = this.state.profileName;
        }

        promiseAjax.get(`/applicationconfig/${profileName}/applications?searchKey=${currentValue}`).then(rsp => {
            const result = rsp.data;
            const data = [];
            result.forEach((r) => {
                data.push({
                    value: `${r.description}(${r.name})`,
                    text: `${r.description}(${r.name})`,
                });
            });
            const start = value.indexOf('(');
            const stop = value.indexOf(')');
            this.setState({
                applicationName: value.substring(start + 1, stop),
                application: data
            });

            let correct = true;
            data.forEach(temp => {
                if (temp.value.indexOf(currentValue) === -1) {
                    correct = false;
                }
            });

            if (!correct || data.length === 0) {
                this.props.form.setFields({
                    application: {
                        value: value,
                        errors: [new Error('此系统不存在！')],
                    },
                });
            }
        })
    }

    handleChange = (value) => {
        this.setState({value});
        this.fetch(value);
        this.checkKeyUnique();
        value = this.subStr(value);
    };

    /**
     * 截取字符串括号内的文字
     */
    subStr = (str) => {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    };

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

        // 清空应用系统
        this.setState({
            applicationName: '',
        });

        const {setFields} = this.props.form;
        setFields({'application': {value: ''}});

    };

    /**
     * 初始化环境下拉菜单
     */
    renderProfileOptions = () => {
        return this.state.profile.map(item => (
            <Option
                key={item.profile}
                value={String(item.profile)}
            >
                {item.name}({item.profile})
            </Option>
        ));
    };

    /**
     * 检查唯一性  是否可用
     * @param ignoreValues
     * @returns {*}
     */
    checkKeyUnique(ignoreValues = []) {
        const {getFieldValue, setFields} = this.props.form;
        if (typeof ignoreValues === 'string') {
            ignoreValues = [ignoreValues];
        }
        return {
            validator(rule, value, callback) {
                if (!value || ignoreValues.indexOf(value) > -1) {
                    return callback();
                }

                const applicationName = getFieldValue('application');
                const profile = getFieldValue('profile');
                if (applicationName === '' || applicationName === undefined || profile === '' || profile === undefined) {
                    return true;
                }

                promiseAjax
                    .get(`/applicationconfig/checkKeyUnique?applicationName=${applicationName}&profile=${profile}`)
                    .then(res => {
                        if (res.data) {
                            callback('抱歉，该数据已经存在！');
                            return;
                        }
                        setFields({'application': {value: applicationName}});
                        setFields({'profile': {value: profile}});
                        callback();
                    });
            },
        };
    }

    handleOk = () => {
        const {saving} = this.state;
        if (saving) return;
        const {form} = this.props;
        form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                values.application = this.subStr(values.application);

                this.setState({saving: true});
                promiseAjax.put('/applicationconfig', values)
                    .then(rsp => {
                        if (rsp.status === 'SUCCESS') {
                            message.success('修改成功', 3);
                            history.back();
                        }
                    }).finally(() => this.setState({saving: false}));
            }
        });
    };

    handleCancel = () => {
        this.setState({
            visible: false,
        });
    };

    /**
     * 返回
     */
    goPageBack = () => {
        history.back();
    };

    render() {
        const {getFieldDecorator} = this.props.form;
        const {saveAndUpdataLoading, applicationConfig} = this.state;
        const {params: {application, profile}} = this.props;
        const options = this.state.application.map(d => <Option key={d.value}>{d.text}</Option>);
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

        return (
            <PageContent>
                <div id="addeditform">
                    <Form onSubmit={this.handleSubmit}>
                        <div className="sub-title">基本配置</div>

                        <Row>
                            <Col span={12}>
                                <FormItem
                                    {...formItemLayout}
                                    label="环境">
                                    {getFieldDecorator('profile', {
                                        onChange: this.handleProfileChange,
                                        initialValue: applicationConfig.profile && applicationConfig.profile.profile,
                                        rules: [
                                            {
                                                required: true, message: '请选择环境!',
                                            },
                                        ],
                                    })(
                                        <Select
                                            disabled={this.state.isEdit}
                                            showSearch
                                            style={{width: '100%'}}
                                            placeholder="请选择环境"
                                            notFoundContent="暂无数据"
                                        >
                                            {this.renderProfileOptions()}
                                        </Select>
                                    )}
                                </FormItem>
                                <FormItem
                                    {...formItemLayout}
                                    label="配置类型">
                                    {getFieldDecorator('sourceType', {
                                        initialValue: applicationConfig.sourceType,
                                        rules: [
                                            {
                                                required: true, message: '请选择配置类型!',
                                            },
                                        ],
                                    })(
                                        <Select
                                            placeholder="配置类型"
                                        >
                                            <Option value="YAML">YAML</Option>
                                        </Select>
                                    )}
                                </FormItem>
                                {
                                    this.state.isEdit ?
                                        <FormItem>
                                            {getFieldDecorator('version', {
                                                initialValue: applicationConfig.version,
                                            })(
                                                <Input type="hidden"/>
                                            )}
                                        </FormItem>
                                        :
                                        ''
                                }
                            </Col>

                            <Col span={12}>
                                <FormItem
                                    {...formItemLayout}
                                    label="应用系统">
                                    {getFieldDecorator('application', {
                                        initialValue: applicationConfig.application && applicationConfig.application.description + '(' + application + ')',
                                        rules: [
                                            {
                                                required: true, message: '请选择应用系统!',
                                            },
                                        ],
                                    })(
                                        <Select
                                            combobox={true}
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
                                <FormItem
                                    {...formItemLayout}
                                    label="备注">
                                    {getFieldDecorator('memo', {
                                        initialValue: applicationConfig.memo,
                                        rules: [
                                            {
                                                required: false, message: '请填写备注!',
                                            },
                                        ],
                                    })(
                                        <Input placeholder="请填写备注" style={{width: '100%'}}/>
                                    )}
                                </FormItem>
                            </Col>
                        </Row>

                        <div className="sub-title">配置内容</div>
                        <Row>
                            <div className="content">
                                <div className="src">
                                    <h4 className="subheader"><a href="#" id="permalink"/></h4>
                                    {getFieldDecorator('propertySource', {
                                        initialValue: applicationConfig.propertySource,
                                        rules: [
                                            {
                                                required: true, message: '请填写配置内容!',
                                            },
                                        ],
                                    })(
                                        <Input id="propertySource" className="configText" type="textarea"
                                               placeholder="请输入配置内容"
                                               rows="12"/>
                                    )}
                                </div>
                                <div className="dst">
                                    <h4 className="subheader">Result (JS object dump):</h4>
                                    <textarea id="result" className="configText"/>
                                </div>
                            </div>
                        </Row>
                        <div style={{marginTop: 20}}>
                            <Button type="primary" size="large" onClick={() => this.handleSubmit()}
                                    loading={saveAndUpdataLoading}
                                    style={{marginRight: 16}}>{this.state.isEdit ? '确定' : '保存'}</Button>

                            <Button
                                type="ghost" htmlType="reset" size="large"
                                onClick={this.resetFieldsForm} style={{marginRight: 16}}>
                                重置
                            </Button>
                            <Button type="primary" size="large"
                                    onClick={this.goPageBack}>返回</Button>
                        </div>
                    </Form>
                </div>
                {
                    this.state.visible ? <Modal
                            title="请确认变更无误后,点击确定按钮保存!"
                            visible={this.state.visible}
                            onOk={this.handleOk}
                            onCancel={this.handleCancel}
                            width='90%'
                            style={{top: 20}}
                        >
                            <DiffComponent
                                profile={profile}
                                application={application}
                                editText={this.state.editConfigText}
                            >
                            </DiffComponent>
                        </Modal>
                        : null
                }
            </PageContent>
        );
    }
}

function mapStateToProps(state) {
    return {
        ...state.frame,
        ...state.pageState.applicationConfig,
    };
}

const LayoutComponent = Form.create()(ApplicationConfigAddEdit);
export default connectComponent({LayoutComponent, mapStateToProps});

import React, {Component} from 'react';
import {Form, Input, Button, Modal, Row, Col, Select, message} from 'antd';
import {PageContent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import jsyaml from 'js-yaml';
import codemirror from 'codemirror';
import 'codemirror/mode/yaml/yaml.js';
import 'codemirror/mode/javascript/javascript.js';
import base64 from '../base64';
import util from 'util';
import connectComponent from '../../../redux/store/connectComponent';
import DiffComponent from './DiffComponent';
import './style.less';

export const PAGE_ROUTE = '/base-information/global-config/+add/:profile';

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

class GlobalConfigAddEdit extends Component {
    state = {
        title: '新增配置',
        isEdit: false,
        globalConfig: [],
        saveAndUpdataLoading: false,
        profile: [],
        profileNames: [],
        visible: false,
        editConfigText: '',
    };

    componentDidMount() {
        // const {profile} = this.props;
        const {params: {profile}} = this.props;
        promiseAjax.get('/globalconfig/profiles').then(rsp => {
            this.setState({
                profile: rsp.data,
            })
        });

        if (profile !== 'addProfile') {
            this.setState({
                title: '修改配置',
                isEdit: true,
            });
            promiseAjax.get(`/globalconfig/${profile}`).then(rsp => {
                if (rsp) {
                    this.setState({
                        globalConfig: rsp.data,
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

    handleSubmit = () => {
        const {form} = this.props;
        form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                const {isEdit} = this.state;
                if (isEdit) {
                    this.setState({
                        visible: true,
                        editConfigText: values.propertySource,
                    });
                } else {
                    promiseAjax.post('/globalconfig', values).then(rsp => {
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
     * 充值表单数据
     * 直接食用restFields不会重置checkbox
     */
    resetFieldsForm = () => {
        this.props.form.resetFields();
        this.setState({
            profileNames: [],
            globalNames: [],
        });
    };


    /**
     * 选择环境
     * @param profileName
     */
    handleProfileChange = (profileName) => {
        this.setState({
            profileName,
        });
    }

    /**
     * 初始化应用系统下拉菜单
     */
    renderProfileOptions = () => {
        return this.state.profile.map(item => <Option key={item.profile}>{item.name}({item.profile})</Option>);
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
                    .get(`/globalconfig/checkKeyUnique/${value}`)
                    .then(res => {
                        if (res.data) {
                            callback('抱歉，该数据已经存在！');
                        }
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
                this.setState({saving: true});
                promiseAjax.put('/globalconfig', values)
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

    render() {
        const {getFieldDecorator} = this.props.form;
        const {params: {profile}} = this.props;
        const {saveAndUpdataLoading, globalConfig} = this.state;
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
                <Form>
                    <div className="sub-title">基本配置</div>
                    <Row>
                        <Col span={12}>
                            <FormItem
                                {...formItemLayout}
                                label="环境">
                                {getFieldDecorator('profile', {
                                    onChange: this.handleProfileChange,
                                    initialValue: globalConfig.profile && globalConfig.profile.profile,
                                    rules: [
                                        {
                                            required: true, message: '请选择环境!',
                                        },
                                        this.state.isEdit ? '' : this.checkKeyUnique(),
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
                                label="备注">
                                {getFieldDecorator('memo', {
                                    initialValue: globalConfig.memo,
                                    rules: [
                                        {
                                            required: false, message: '请填写备注!',
                                        },
                                    ],
                                })(
                                    <Input placeholder="请填写备注" style={{width: '100%'}}/>
                                )}
                            </FormItem>
                            {
                                this.state.isEdit ?
                                    <FormItem>
                                        {getFieldDecorator('version', {
                                            initialValue: globalConfig.version,
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
                                label="配置类型">
                                {getFieldDecorator('sourceType', {
                                    initialValue: globalConfig.sourceType,
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
                        </Col>
                    </Row>

                    <div className="sub-title">配置内容</div>
                    <Row>
                        <div className="content">
                            <div className="src">
                                <h4 className="subheader"><a href="#" id="permalink"/></h4>
                                {getFieldDecorator('propertySource', {
                                    initialValue: globalConfig.propertySource,
                                    rules: [
                                        {
                                            required: true, message: '请填写配置内容!',
                                        },
                                    ],
                                })(
                                    <Input id="propertySource" className="configText" type="textarea" placeholder="请输入配置内容"
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
                                loading={saveAndUpdataLoading} style={{marginRight: 16}}>{this.state.isEdit ? '确定' : '保存'}</Button>
                        <Button
                            type="ghost" htmlType="reset" size="large"
                            onClick={this.resetFieldsForm} style={{marginRight: 16}}>
                            重置
                        </Button>
                        <Button type="primary" size="large" onClick={() => {
                            history.back();
                        }}>返回</Button>
                    </div>
                </Form>
                {
                    this.state.visible ? (
                        <Modal
                            title="请确认变更无误后,点击确定按钮保存!"
                            visible={this.state.visible}
                            onOk={this.handleOk}
                            onCancel={this.handleCancel}
                            width='90%'
                            style={{top: 20}}
                        >
                            <DiffComponent
                                profile={profile}
                                editText={this.state.editConfigText}
                            >
                            </DiffComponent>
                        </Modal>
                    ) : null
                }
            </PageContent>
        );
    }
}

function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

const LayoutComponent = Form.create()(GlobalConfigAddEdit);
export default connectComponent({LayoutComponent, mapStateToProps});

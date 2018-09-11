import React from 'react';
import {promiseAjax} from 'sx-ui';
import {Form, Select} from 'antd';

const FormItem = Form.Item;
const Option = Select.Option;

let timeout;
let currentValue;


class ApplicationQuery extends React.Component {
    state = {
        // applications: [],
        data: [],
        value: '',
    };

    componentWillMount() {

    }

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
            this.setState({data})
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
        this.props.handleApplicationChange(applicationName);
    }

    render() {
        const formItemLayout = this.props.formItemLayout;
        const {getFieldDecorator} = this.props.getForm;
        const options = this.state.data.map(d => <Option key={d.value}>{d.text}</Option>);

        return (
            <div>
                <FormItem
                    {...formItemLayout}
                    label="应用系统"
                >
                    {getFieldDecorator('application', {})(
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
                            {options}
                        </Select>
                    )}
                </FormItem>
            </div>
        )
    }
}

export default ApplicationQuery;
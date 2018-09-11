import React from 'react';
import {promiseAjax} from 'sx-ui';
import {Form, Select} from 'antd';

const FormItem = Form.Item;
const Option = Select.Option;

class ProFileQuery extends React.Component {
    state = {
        profiles: [],
    };

    componentDidMount() {
        promiseAjax.get('applicationconfig/profiles').then(rsp => {
            this.setState({
                profiles: rsp.data,
            })
        })
    }

    /**
     * 选择环境
     * @param profileName
     */
    handleChange = (profileName) => {
        const start = profileName.indexOf('(');
        const stop = profileName.indexOf(')');
        this.props.handleProfileChange(profileName.substring(start + 1, stop));
    }

    /**
     * 初始化环境下拉菜单
     */
    renderProfileOptions = () => {
        return this.state.profiles.map(item => {
            const keyStr = item.name + '(' + item.profile + ')';
            return <Option key={keyStr}>{keyStr}</Option>;
        });
    };

    onChange = (field, value) => {
        this.setState({
            [field]: value,
        });
    };

    render() {
        const formItemLayout = this.props.formItemLayout;
        const {getFieldDecorator} = this.props.getForm;
        return (
            <div>
                <FormItem
                    {...formItemLayout}
                    label="环境">
                    {getFieldDecorator('profile', {
                        onChange: this.handleChange,
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
            </div>
        )
    }
}

export default ProFileQuery;
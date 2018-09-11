import React, {Component} from 'react';
import {Form, Row, Col, Spin, Button, Input, Popconfirm, message} from 'antd';
import {PageContent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

export const PAGE_ROUTE = '/base-information/global-config/+detail/:profile/:historyId';

class Detail extends Component {
    state = {
        globalConfig: [],
        gettingGlobalConfig: false,
        isHistory: false,
    };


    componentWillMount() {
        this.setState({
            gettingGlobalConfig: true,
        });
        // const {profile, historyId} = this.props;
        const {params: {profile, historyId}} = this.props;
        if (historyId !== 'historyId') {
            promiseAjax.get(`/globalconfiglog/${historyId}`).then(rsp => {

                if (rsp.status) {
                    this.setState({
                        isHistory: true,
                        globalConfig: rsp.data,
                        gettingGlobalConfig: false,
                    });
                }
            });
        } else {
            promiseAjax.get(`/globalconfig/${profile}`).then(rsp => {
                if (rsp.status) {
                    this.setState({
                        globalConfig: rsp.data,
                        gettingGlobalConfig: false,
                    });
                }
            });
        }

    }

    /**
     * 替换当前版本
     */
    handleReplace = () => {
        // const {historyId} = this.props;
        const {params: {historyId}} = this.props;

        promiseAjax.put(`/globalconfig/${historyId}`).then(rsp => {
            this.setState({
                isHistory: false,
            });

            if (rsp.status === 'SUCCESS') {
                message.success('替换成功', 3);
                history.back();
            }
            // this.props.handleSave(false);
        })
    };

    render() {
        const {gettingGlobalConfig, globalConfig} = this.state;
        const labelStyle = {
            textAlign: 'right',
            marginBottom: 18,
            fontWeight: 'bold',
        };
        return (
            <PageContent>
                <Form>
                    <Spin spinning={gettingGlobalConfig}>
                        <div className="sub-title">基本配置</div>
                        <Row>
                            <Col span={12}>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        环境：
                                    </Col>
                                    <Col span={18}>
                                        {globalConfig.profile && globalConfig.profile.name + '(' + globalConfig.profile.profile + ')'}
                                    </Col>
                                </Row>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        配置类型：
                                    </Col>
                                    <Col span={18}>
                                        {globalConfig.sourceType}
                                    </Col>
                                </Row>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        版本号：
                                    </Col>
                                    <Col span={18}>
                                        {globalConfig.version}
                                    </Col>
                                </Row>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        备注：
                                    </Col>
                                    <Col span={18}>
                                        {globalConfig.memo}
                                    </Col>
                                </Row>
                            </Col>
                        </Row>
                        <div className="sub-title">配置内容</div>
                        <Row>
                            <Input type="textarea" value={globalConfig.propertySource} disabled rows="12"/>
                        </Row>
                        <div style={{marginTop: 20}}>
                            <Button type="primary" size="large" onClick={() => {
                                history.back();
                            }}
                                    style={{marginRight: '16px'}}>返回</Button>
                            {
                                this.state.isHistory ? <Popconfirm
                                    placement="topRight"
                                    title={<span>你确定还原至当前版本吗？</span>}
                                    onConfirm={() => this.handleReplace()}
                                >
                                    <Button type="primary" size="large">替换当前版本</Button>
                                </Popconfirm> : ''
                            }
                        </div>
                    </Spin>
                </Form>
            </PageContent>
        );
    }
}

function mapStateToProps(state) {
    return {
        ...state.frame,
    };
}

const LayoutComponent = Form.create()(Detail);
export default connectComponent({LayoutComponent, mapStateToProps});

import React, {Component} from 'react';
import {Form, Row, Col, Spin, Button, Input, Popconfirm, message} from 'antd';
import {PageContent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

export const PAGE_ROUTE = '/base-information/application-config/+detail/:profile/:application/:historyId';

class Detail extends Component {
    state = {
        applicationConfig: [],
        gettingApplicationConfig: false,
        isHistory: false,
    };


    componentWillMount() {
        this.setState({
            gettingApplicationConfig: true,
        });
        const {params: {profile, application, historyId}} = this.props;
        console.log(historyId, profile, application);

        if (historyId !== 'historyId') {
            promiseAjax.get(`/applicationconfiglog/${historyId}`).then(rsp => {
                if (rsp.status) {
                    this.setState({
                        isHistory: true,
                        applicationConfig: rsp.data,
                        gettingApplicationConfig: false,
                    });
                }
            });
        } else {
            promiseAjax.get(`/applicationconfig/${application}/${profile}`).then(rsp => {
                if (rsp.status) {
                    this.setState({
                        applicationConfig: rsp.data,
                        gettingApplicationConfig: false,
                    });
                }
            });
        }

    }

    /**
     * 替换当前版本
     */
    handleReplace = () => {
        // const {applicationConfig} = this.state;
        // const {historyId} = this.props;
        const {params: {historyId}} = this.props;

        /*const params = {
            application: applicationConfig.application.name,
            profile: applicationConfig.profile.profile,
            memo: applicationConfig.memo,
            sourceType: applicationConfig.sourceType,
            propertySource: applicationConfig.propertySource,
            version: applicationConfig.version,
        };
        */
        promiseAjax.put(`/applicationconfig/${historyId}`).then(rsp => {
            this.setState({
                isHistory: false,
            });

            if (rsp.status === 'SUCCESS') {
                message.success('替换成功', 3);
                history.back();
            }
        })
    };

    /**
     * 返回
     */
    goPageBack = () => {
        history.back();
    };

    render() {
        const {gettingApplicationConfig, applicationConfig} = this.state;
        const labelStyle = {
            textAlign: 'right',
            marginBottom: 18,
            fontWeight: 'bold',
        };
        return (
            <PageContent>
                <Form>
                    <Spin spinning={gettingApplicationConfig}>
                        <div className="sub-title">基本配置</div>
                        <Row>
                            <Col span={12}>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        应用系统：
                                    </Col>
                                    <Col span={18}>
                                        {applicationConfig.application && applicationConfig.application.description + '(' + applicationConfig.application.name + ')'}
                                    </Col>
                                </Row>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        配置类型：
                                    </Col>
                                    <Col span={18}>
                                        {applicationConfig.sourceType}
                                    </Col>
                                </Row>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        版本号：
                                    </Col>
                                    <Col span={18}>
                                        {applicationConfig.version}
                                    </Col>
                                </Row>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        环境：
                                    </Col>
                                    <Col span={18}>
                                        {applicationConfig.profile && applicationConfig.profile.name + '(' + applicationConfig.profile.profile + ')'}
                                    </Col>
                                </Row>
                                <Row>
                                    <Col span={6} style={labelStyle}>
                                        备注：
                                    </Col>
                                    <Col span={18}>
                                        {applicationConfig.memo}
                                    </Col>
                                </Row>
                            </Col>

                        </Row>
                        <div className="sub-title">配置内容</div>
                        <Row>
                            <Input type="textarea" value={applicationConfig.propertySource} disabled rows="12"/>
                        </Row>
                        <div style={{marginTop: 20}}>
                            <Button type="primary" size="large" onClick={this.goPageBack}
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
        ...state.pageState.applicationConfig,
    };
}

const LayoutComponent = Form.create()(Detail);
export default connectComponent({LayoutComponent, mapStateToProps});

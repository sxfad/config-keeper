import React, {Component} from 'react';
import {Form, Button, Popconfirm, message} from 'antd';
import {PageContent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import qs from 'query-string';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

export const PAGE_ROUTE = '/base-information/application-config/+diff/:profile/:application/:historyId';

class Diff extends Component {
    state = {
        historyConfigText: '',
        currentConfigText: '',
        historyVersionId: '',
        currentVersionId: '',
    };

    componentDidMount() {
        const {params: {profile, application, historyId}} = this.props;

        const search = qs.parse(window.location.search);
        let {secondId} = search;

        const showDiffIframe = () => {
            const iframe = document.createElement('iframe');
            iframe.src = '/static/diffhtml.html';
            iframe.width = '100%';
            iframe.setAttribute("frameborder", 0);
            iframe.setAttribute("id", 'diffIframe');
            document.getElementById('prettydiff').appendChild(iframe);
        };

        // 选择两个历史版本进行对比了
        if (secondId) {
            promiseAjax.get(`/applicationconfiglog/${historyId}`).then(rsp => {
                if (rsp.status) {
                    this.setState({
                        currentConfigText: rsp.data.propertySource,
                        currentVersionId: rsp.data.version,
                    });
                    promiseAjax.get(`/applicationconfiglog/${secondId}`).then(rsp => {
                        if (rsp.status) {
                            this.setState({
                                historyConfigText: rsp.data.propertySource,
                                historyVersionId: rsp.data.version,
                                allIsHistory: true,
                            }, showDiffIframe);
                        }
                    });
                }
            });


        } else {
            // 只选择了一个历史版本，跟当前版本进行对比
            promiseAjax.get(`/applicationconfig/${application}/${profile}`).then(rsp => {
                if (rsp.status) {
                    this.setState({
                        currentConfigText: rsp.data.propertySource,
                        currentVersionId: rsp.data.version,
                    });
                    promiseAjax.get(`/applicationconfiglog/${historyId}`).then(rsp => {
                        if (rsp.status) {
                            this.setState({
                                historyConfigText: rsp.data.propertySource,
                                historyVersionId: rsp.data.version,
                                allIsHistory: false,
                            }, showDiffIframe);
                        }
                    });
                }
            });
        }
    }

    /**
     * 替换历史版本
     */
    handleReplace = () => {
        const {params: {historyId}} = this.props;
        promiseAjax.put(`/applicationconfig/${historyId}`).then(rsp => {
            if (rsp.status === 'SUCCESS') {
                message.success('替换成功', 3);
                window.history.back();
            }
        })
    }

    render() {
        const {
            currentConfigText,
            historyConfigText,
            historyVersionId,
            currentVersionId,
            allIsHistory,
        } = this.state;
        return (
            <PageContent>
                <div id="prettydiff"/>
                <div>
                    <input style={{display: 'none'}} id="historyConfigText" value={historyConfigText}/>
                    <input style={{display: 'none'}} id="currentConfigText" value={currentConfigText}/>

                    <input style={{display: 'none'}} id="historyId" value={historyVersionId}/>
                    <input style={{display: 'none'}} id="currentId" value={currentVersionId}/>
                    <input style={{display: 'none'}} id="allIsHistory" value={allIsHistory}/>
                </div>
                <div>
                    <Button type="primary" size="large" onClick={() => {
                        window.history.back();
                    }}
                            style={{marginRight: '16px'}}>返回</Button>
                    {
                        <Popconfirm
                            placement="topRight"
                            title={<span>你确定还原至历史版本吗？</span>}
                            onConfirm={() => this.handleReplace()}
                        >
                            <Button type="primary" size="large">替换历史版本</Button>
                        </Popconfirm>
                    }
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

const LayoutComponent = Form.create()(Diff);
export default connectComponent({LayoutComponent, mapStateToProps});

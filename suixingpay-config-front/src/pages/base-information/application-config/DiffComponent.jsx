import React, {Component} from 'react';
import {Form} from 'antd';
import {PageContent} from 'sx-ui/antd';
import {promiseAjax} from 'sx-ui';
import connectComponent from '../../../redux/store/connectComponent';
import './style.less';

class DiffComponent extends Component {
    state = {
        historyConfigText: '',
        currentConfigText: '',
        historyVersionId: '',
        currentVersionId: '',
    };

    componentWillMount() {
    }

    componentDidMount() {
        const {profile, application, editText} = this.props;
        promiseAjax.get(`/applicationconfig/${application}/${profile}`).then(rsp => {
            if (rsp.status) {
                this.setState({
                    historyConfigText: rsp.data.propertySource,
                    historyVersionId: rsp.data.version
                }, () => {
                    const iframe = document.createElement('iframe');
                    iframe.src = '/static/diffhtml.html';
                    iframe.width = '100%';
                    iframe.setAttribute("frameborder", 0);
                    iframe.setAttribute("id", 'diffIframe');
                    document.getElementById('prettydiff').appendChild(iframe);
                });
            }
        });

        this.setState({
            currentConfigText: editText,
            currentVersionId: '当前修改内容',
        });
    }

    render() {
        const {currentConfigText, historyConfigText, historyVersionId, currentVersionId} = this.state;
        return (
            <PageContent>
                <div id="prettydiff" style={{marginTop: -30, marginBottom: -30}}/>
                <div>
                    <input style={{display: 'none'}} id="historyConfigText" value={historyConfigText}/>
                    <input style={{display: 'none'}} id="currentConfigText" value={currentConfigText}/>

                    <input style={{display: 'none'}} id="historyId" value={historyVersionId}/>
                    <input style={{display: 'none'}} id="currentId" value={currentVersionId}/>
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

const LayoutComponent = Form.create()(DiffComponent);
export default connectComponent({LayoutComponent, mapStateToProps});

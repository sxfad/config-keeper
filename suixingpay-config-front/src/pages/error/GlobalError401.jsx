import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {Button} from 'antd';
import error401 from './401.png';
import './style.less';

export class GlobalError401 extends Component {
    state = {
        remainSecond: 9,
    };

    componentDidMount() {
        this.si = setInterval(() => {
            let {remainSecond} = this.state;
            remainSecond--;
            if (remainSecond <= 0) {
                this.clear();
                window.parent.sysLogin();
            }
            this.setState({
                remainSecond,
            });
        }, 1000);
    }

    componentWillUnmount() {
        this.clear();
    }

    clear() {
        if (this.si) {
            clearInterval(this.si);
        }
    }

    render() {
        const {remainSecond} = this.state;
        return (
            <div className="error-page">
                <img src={error401} alt="401图片"/>
                <p className="error-text">您还未登录，请您先登录！</p>
                <p className="error-text error-sub-text">{remainSecond} 秒后跳转到登录页面...</p>
                <Button
                    type="primary"
                    className="error-btn error-btn-right"
                    onClick={() => {
                        window.parent.sysLogin();
                    }}
                >
                    马上登录
                </Button>
            </div>
        )
            ;
    }
}


ReactDOM.render(<GlobalError401/>, document.getElementById('main'));

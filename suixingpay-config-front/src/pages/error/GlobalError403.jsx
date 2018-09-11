import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {Button} from 'antd';
import error403 from './403.png';
import './style.less';

class GlobalError403 extends Component {
    state = {};

    render() {
        return (
            <div className="error-page">
                <img src={error403} alt="401图片"/>
                <p className="error-text">非常抱歉，您没权访问此页面！</p>
                <Button
                    type="primary"
                    className="error-btn"
                    onClick={() => {
                        window.history.back();
                    }}
                >
                    返回
                </Button>
            </div>
        );
    }
}

ReactDOM.render(<GlobalError403/>, document.getElementById('main'));

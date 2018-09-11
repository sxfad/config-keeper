import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {Button} from 'antd';
import error500 from './500.png';
import './style.less';

class GlobalError500 extends Component {
    state = {};

    render() {
        return (
            <div className="error-page">
                <img src={error500} alt="400图片"/>
                <p className="error-text">系统故障，请联系相关人员！</p>
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

ReactDOM.render(<GlobalError500/>, document.getElementById('main'));

# 路由封装

做大型应用时，route比较多，写在一个routes.js文件中，一是routes.js会过于庞大，不好维护，二是团队协作时，很容易产生冲突。
因此每个模块的路由，写在自己的模块下(以routes.js命名)，无法在各个模块routes.js中定义的router，统一在`src/routes.js`中定义。
所有的路由最终通过脚本规整到`src/all-routes.js`文件下。

jsx文件头部定义`export const PAGE_ROUTE = '/base-information/users';`，可以不必编写`routes.js`文件。只是生成如下结构：
```
{
    path: '/base-information/users',
    getComponent: ...(异步写法),
},
```
如果需要其他路由属性，请在routes.js文件中定义路由，同时页面一定不要写`export const PAGE_ROUTE = ...`，否则路由被定义两遍，会产生意料之外的bug。

- 后端所有http的get请求，没有被截获的都渲染`index.html`
    ```
    node后端路由配置（routes.js）：
    router.get('*', function (req, res, next) {
        // ajax请求 抛出404,其他请求直接render index.html
        res.render('index.html');
    });
    ```
- 前端所有没截获的path，都渲染Error404组件
    ```javascript
    // src/Router.jsx
    pageRouts.push(
        {
            path: '*',
            getComponent: (location, cb) => {
                require.ensure([], (require) => {
                    cb(null, connectComponent(require('./layouts/error/Error404')));
                });
            },
        }
    );
    ```
- 页面跳转使用`Link`，或者`this.props.router`相关API，否则会跳出单页面应用
    ```javascript
    import {Link} from 'react-router'
    <Link to="/xxxxx">XXXXX</Link>

    this.props.router.push('/user/add');
    ```


## 页面离开提示
一般可以用于编辑页面，当路由切换到其它页面前，如果有未保存内容，提示用户是否放弃保存。
```javascript
...
static contextTypes = {
    router: PropTypes.object,
};
...
componentDidMount() {

    const {route} = this.props;
    const {router} = this.context; // If contextTypes is not defined, then context will be an empty object.

    router.setRouteLeaveHook(route, (/* nextLocation */) => {
        /* eslint-disable */
        const truthBeTold = window.confirm('您有未保存的内容，将保存未草稿，确定离开此页面？');
        /* eslint-enable */
        if (truthBeTold) {
            // 点击了确定
            return true;
        }
        // 恢复地址栏
        window.history.pushState({}, '', route.path);
        // 离开当前页面
        return false;
        // 返回 false 会继续停留当前页面，
        // 否则，返回一个字符串，会显示给用户，让其自己决定
    });
}
...
```

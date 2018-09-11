/*
 * 自定义路由loader
 * 添加 startFetchingComponent，shouldComponentMount，endFetchingComponent hock，这三个方法来自于 src/utils/route-utils
 * 组件使用connectComponent与redux做链接
 * asyncComponent: './user/UserList',
 * ===>
 * getComponent: (nextState, cb) => {
 *     startFetchingComponent();
 *     require.ensure([], (require) => {
 *         if (!shouldComponentMount(nextState)) return;
 *         endFetchingComponent();
 *         cb(null, connectComponent(require('./user/UserList')));
 *     });
 * },
 * */
const utils = require('./utils');

module.exports = function (source, other) {
    this.cacheable();
    let routesStrTemp = source;
    const patt = /asyncComponent:[ ]*['"]([^'"]+)['"][,]/gm;
    let isRoutes = false;
    let block = null;
    while ((block = patt.exec(source)) !== null) {
        isRoutes = block[0] && block[1];
        if (isRoutes) {
            routesStrTemp = routesStrTemp.replace(block[0], utils.getComponentString(block[1]));
        }
    }
    if (isRoutes) {
        routesStrTemp = utils.getRouteAddtionsImportString() + routesStrTemp;
        this.callback(null, routesStrTemp, other);
    } else {
        this.callback(null, source, other);
    }
};
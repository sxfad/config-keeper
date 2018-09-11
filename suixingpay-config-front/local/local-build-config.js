/*
 * 只开发模式有效
 * 部分构建的本地化配置，满足不同人不同配置，而不产生冲突
 * */

const path = require('path');
const srcPath = './src';

module.exports = {
    routesIgnore: [ // 忽略文件，不进行构建，提供部分模块打包功能
        // '**/ActionsExample.jsx',
    ],
    // pagePath: path.join(srcPath, 'pages/**/*.jsx'), // 使用了PAGE_ROUTE INIT_STATE 文件所在目录，与routesIgnore同样可以控制打包模块
    pagePath: path.join(srcPath, '**/*.jsx'),
}

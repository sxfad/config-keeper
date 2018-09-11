# 构建脚本

## dll
开发模式使用dll优化rebuild速度，大概能够提高1倍（40个页面以上）
生产环境不使用dll，dll将所有的第三方打包，2.7M，太大，如果不适用dll，webpack2本身有优化，没有使用到的export 代码不会打包
如果使用dll，将多出近2M的无用资源。
生产环境简单使用：CommonsChunkPlugin

## 分模块打包
config.js中进行如下配置，可以进行分模块打包
```
routesIgnore: routesIgnore, // 忽略含有路由的文件，不进行构建，提供部分模块打包功能，提高reBuild速度
pagePath: pagePath, // 使用了PAGE_ROUTE INIT_STATE 文件所在目录，与routesIgnore同样可以控制打包模块
```

## [css module](https://github.com/webpack-contrib/css-loader)
通过`include`、`exclude`对less文件构建进行区分，可以做到指定模块启用css module

## 热刷新 热替换

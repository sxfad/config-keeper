const path = require('path');
const webpack = require('webpack');
const merge = require('webpack-merge');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const AddAssetHtmlPlugin = require('add-asset-html-webpack-plugin');
const config = require('./config');
const baseWebpackConfig = require('./webpack.base.conf');

const sourcePath = path.resolve(__dirname, '../', 'src');

Object.keys(baseWebpackConfig.entry).forEach(name => {
    // add hot-reload related code to entry chunks
    baseWebpackConfig.entry[name] = [path.join(__dirname, './dev-client')].concat(baseWebpackConfig.entry[name]);
});

module.exports = merge(baseWebpackConfig, {
    output: {
        path: path.join(__dirname, '../', 'dist'),
        publicPath: config.dev.assetsPublicPath,
        filename: '[name].js',
    },
    devtool:'source-map',
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader', 'postcss-loader'],
            },
            {
                test: /\.less/,
                exclude: path.resolve(__dirname, '../', 'src/pages/examples/cssModule'),
                use: [
                    'style-loader',
                    'css-loader',
                    'postcss-loader',
                    {
                        loader: 'less-loader',
                        options: {
                            sourceMap: false,
                            includePaths: [sourcePath],
                        },
                    },
                ],
            },
            {
                test: /\.less/,
                include: path.resolve(__dirname, '../', 'src/pages/examples/cssModule'),
                use: [
                    'style-loader',
                    {
                        // https://github.com/webpack-contrib/css-loader
                        loader: 'css-loader',
                        options: {
                            module: true,
                            camelCase: true,
                            localIdentName: '[path][name]-[local]',
                        },
                    },
                    'postcss-loader',
                    {
                        loader: 'less-loader',
                        options: {
                            sourceMap: false,
                            includePaths: [sourcePath],
                        },
                    },
                ],
            },
        ],
    },
    plugins: [
        // 只在开发模式下使用
        // webpack2会忽略未使用的export，打包文件更小
        // dll会全部打包，会有很多未使用代码
        // 这个项目会相差1.7M
        new webpack.DllReferencePlugin({
            context: '.',
            manifest: require(path.join(__dirname, '../', 'public', 'vendor-manifest.json')),
        }),
        new AddAssetHtmlPlugin({
            filepath: path.join(__dirname, '../', 'public', 'vendor.dll.js'),
            includeSourcemap: false,
            hash: true,
        }),
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoEmitOnErrorsPlugin(),
        new HtmlWebpackPlugin({
            chunks: ['common', 'app'],
            favicon: './favicon.png',
            filename: 'index.html',
            template: './index.html',
            title: '随行付统一配置中心',
            inject: true,
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'signin'],
            template: './signin.html',
            filename: 'signin.html',
            favicon: './favicon.png',
            title: '登录',
            inject: true,
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'globalError403'],
            template: './index.html',
            filename: 'error-403.html',
            favicon: './favicon.png',
            title: '无权限',
            inject: true,
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'globalError401'],
            template: './index.html',
            filename: 'error-401.html',
            favicon: './favicon.png',
            title: '重新登录',
            inject: true,
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'globalError500'],
            template: './index.html',
            filename: 'error-500.html',
            favicon: './favicon.png',
            title: '系统故障',
            inject: true,
        }),

        new webpack.optimize.CommonsChunkPlugin({
            name: 'common',
            filename: 'common.js',
        }),
    ]
});

const path = require('path');
const config = require('./config');
const utils = require('./utils');
const webpack = require('webpack');
const merge = require('webpack-merge');
const baseWebpackConfig = require('./webpack.base.conf');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const os = require('os');
const UglifyJsParallelPlugin = require('webpack-uglify-parallel');

const sourcePath = path.resolve(__dirname, '../', 'src');

// Create multiple instances
const extractCSS = new ExtractTextPlugin({
    filename: utils.assetsPath('css/[name].[contenthash].css'),
    disable: false,
    allChunks: true,
    ignoreOrder: false,
});
const extractCSSModule = new ExtractTextPlugin({
    filename: utils.assetsPath('css/[name].[contenthash].css'),
    disable: false,
    allChunks: true,
    ignoreOrder: true,
});

const webpackConfig = merge(baseWebpackConfig, {
    devtool: config.build.productionSourceMap ? '#source-map' : false,
    output: {
        path: config.build.assetsRoot,
        publicPath: config.build.assetsPublicPath,
        filename: utils.assetsPath('js/[name].[chunkhash].js'),
        chunkFilename: utils.assetsPath('js/[id].[chunkhash].js')
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                loader: extractCSS.extract({
                    fallback: 'style-loader',
                    use: ['css-loader', 'postcss-loader'],
                })
            },
            {
                test: /\.less$/,
                exclude: path.resolve(__dirname, '../', 'src/pages/examples/cssModule'),
                loader: extractCSS.extract({
                    fallback: 'style-loader',
                    use: [
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
                })
            },
            {
                test: /\.less/,
                include: path.resolve(__dirname, '../', 'src/pages/examples/cssModule'),
                loader: extractCSSModule.extract({
                    fallback: 'style-loader',
                    use: [
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
                }),
            },
        ],
    },
    plugins: [
        extractCSS,
        extractCSSModule,
        // 多线程压缩：83.04s 好像差别不大
        // new UglifyJsParallelPlugin({
        //     workers: os.cpus().length, // usually having as many workers as cpu cores gives good results
        //     compress: {
        //         warnings: false
        //     },
        // }),

        // webpack提供的压缩：88.29s
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                warnings: false
            }
        }),
        // split vendor js into its own file
        new webpack.optimize.CommonsChunkPlugin({
            name: 'common',
            minChunks: function (module, count) {
                // any required modules inside node_modules are extracted to vendor
                return (
                    module.resource &&
                    /\.js$/.test(module.resource) &&
                    module.resource.indexOf(
                        path.join(__dirname, '../node_modules')
                    ) === 0
                )
            }
        }),
        // extract webpack runtime and module manifest to its own file in order to
        // prevent vendor hash from being updated whenever app bundle is updated
        new webpack.optimize.CommonsChunkPlugin({
            name: 'common-manifest',
            chunks: ['common']
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'common-manifest', 'app'],
            favicon: './favicon.png',
            filename: 'index.html',
            template: './index.html',
            title: '随行付统一配置中心',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
                // more options:
                // https://github.com/kangax/html-minifier#options-quick-reference
            },
            // necessary to consistently work with multiple chunks via CommonsChunkPlugin
            chunksSortMode: 'dependency',
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'common-manifest', 'signin'],
            template: './signin.html',
            filename: 'signin.html',
            favicon: './favicon.png',
            title: '登录',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
                // more options:
                // https://github.com/kangax/html-minifier#options-quick-reference
            },
            // necessary to consistently work with multiple chunks via CommonsChunkPlugin
            chunksSortMode: 'dependency',
        }),

        new HtmlWebpackPlugin({
            chunks: ['common', 'common-manifest', 'passwordRetrieval'],
            template: './index.html',
            filename: 'password-retrieval.html',
            favicon: './favicon.png',
            title: '找回密码',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
                // more options:
                // https://github.com/kangax/html-minifier#options-quick-reference
            },
            // necessary to consistently work with multiple chunks via CommonsChunkPlugin
            chunksSortMode: 'dependency',
        }),

        new HtmlWebpackPlugin({
            chunks: ['common', 'common-manifest', 'globalError403'],
            template: './index.html',
            filename: 'error-403.html',
            favicon: './favicon.png',
            title: '无权限',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
                // more options:
                // https://github.com/kangax/html-minifier#options-quick-reference
            },
            // necessary to consistently work with multiple chunks via CommonsChunkPlugin
            chunksSortMode: 'dependency',
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'common-manifest', 'globalError401'],
            template: './index.html',
            filename: 'error-401.html',
            favicon: './favicon.png',
            title: '重新登录',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
                // more options:
                // https://github.com/kangax/html-minifier#options-quick-reference
            },
            // necessary to consistently work with multiple chunks via CommonsChunkPlugin
            chunksSortMode: 'dependency',
        }),
        new HtmlWebpackPlugin({
            chunks: ['common', 'common-manifest', 'globalError500'],
            template: './index.html',
            filename: 'error-500.html',
            favicon: './favicon.png',
            title: '系统故障',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
                // more options:
                // https://github.com/kangax/html-minifier#options-quick-reference
            },
            // necessary to consistently work with multiple chunks via CommonsChunkPlugin
            chunksSortMode: 'dependency',
        }),
    ],
});

if (config.build.productionGzip) {
    const CompressionWebpackPlugin = require('compression-webpack-plugin')

    webpackConfig.plugins.push(
        new CompressionWebpackPlugin({
            asset: '[path].gz[query]',
            algorithm: 'gzip',
            test: new RegExp(
                '\\.(' +
                config.build.productionGzipExtensions.join('|') +
                ')$'
            ),
            threshold: 10240,
            minRatio: 0.8
        })
    )
}

module.exports = webpackConfig

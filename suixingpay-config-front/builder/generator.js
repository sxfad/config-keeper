const chokidar = require('chokidar');
const fs = require('fs');
const config = require('./config');
const generateAllRoutes = require('./generate-all-routes');
const generatePageRoute = require('./generate-page-route');
const generatePageInitState = require('./generate-page-init-state');

const routesSourceFileName = config.routesFileName;
const pageSourceFileName = config.pagePath;
const allRoutesFileName = config.allRoutesFileName;
const pageInitStateFileName = config.pageInitStateFileName;
const pageRouteFileName = config.pageRouteFileName;

// 删除历史生成文件
fs.existsSync(allRoutesFileName) && fs.unlinkSync(allRoutesFileName);
fs.existsSync(pageInitStateFileName) && fs.unlinkSync(pageInitStateFileName);
fs.existsSync(pageRouteFileName) && fs.unlinkSync(pageRouteFileName);

generate();

function generate() {
    generateAllRoutes.generateAllRoutes();
    generatePageRoute.generateAllPageRoute();
    generatePageInitState.generateAllInitState();
}

exports.generate = generate;
exports.watch = function () {
    if (process.env.NODE_ENV === 'development' || process.env.NODE_ENV === 'dev') {
        chokidar.watch([routesSourceFileName, pageSourceFileName], {ignored: config.routesIgnore}).on('all', (event, pathName) => {
            generateAllRoutes.handleWatchAllRoutes(event, pathName);
            generatePageInitState.handlePageInitStateWatch(event, pathName);
            generatePageRoute.handlePageRouteWatch(event, pathName);
        });
    }
}
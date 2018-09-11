/* eslint-disable */
const fs = require('fs');
const path = require('path');
const glob = require('glob');
const utils = require('./utils');
const config = require('./config');

const sourceFileName = config.pagePath;
const targetFileName = config.pageRouteFileName;

const paths = {};
const pathNames = {};
exports.handlePageRouteWatch = function (event, pathName) {
    const routePath = getRoutePath(pathName);

    if (!routePath) return;
    console.log('page-route:', event, pathName);
    const pn = utils.getPathName(pathName);
    if (event === 'add' || event === 'change') {
        paths[pathName] = routePath;
        pathNames[pathName] = pn;

        const ps = Object.keys(paths).map(function (key) {
            return paths[key];
        });
        const pns = Object.keys(pathNames).map(function (key) {
            return pathNames[key];
        });
        writeAllPageRoute(ps, pns, targetFileName);
    }
    if (event === 'unlink') {
        delete paths[pathName];
        delete pathNames[pathName];

        const ps2 = Object.keys(paths).map(function (key) {
            return paths[key];
        });
        const pns2 = Object.keys(pathNames).map(function (key) {
            return pathNames[key];
        });
        writeAllPageRoute(ps2, pns2, targetFileName);
    }
}
exports.generateAllPageRoute = function () {
    const result = getPathsAndPathNames(sourceFileName, targetFileName, getRoutePath);
    const paths = result.paths;
    const pathNames = result.pathNames;
    writeAllPageRoute(paths, pathNames, targetFileName);
}

function writeAllPageRoute(paths, pathNames, targetFileName) {
    let fileString = '';
    fileString = utils.getRouteAddtionsImportString();

    fileString += 'export default [';
    pathNames.forEach(function (im, i) {
        fileString += '\n    {\n        ';
        fileString += 'path: \'' + paths[i] + '\',\n        ';
        fileString += utils.getComponentString(im);
        // fileString += 'asyncComponent: \'' + im + '\',\n    ';
        fileString += '},'
    });
    fileString += '\n];\n';
    fs.writeFileSync(targetFileName, fileString);
}


function getRoutePath(file) {
    try {
        const fileStr = fs.readFileSync(file);
        const patt = /export const PAGE_ROUTE = [ ]*['"]([^'"]+)['"][;]/gm;
        let isRoutes = false;
        let block = null;
        while ((block = patt.exec(fileStr)) !== null) {
            isRoutes = block[0] && block[1];
            if (isRoutes) {
                return block[1];
            }
        }
        return false;
    } catch (e) {
        return true; // 文件被移除之后，也算他没有PAGE_ROUTE
    }
}

function getPathsAndPathNames(sourceFilePath, targetFileName, filter) {
    filter = filter || function () {
            return true
        };
    const paths = [];
    const pathNames = [];
    const files = glob.sync(sourceFilePath, {ignore: config.routesIgnore});
    if (files && files.length) {
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            const p = filter(file);
            if (p && p !== true) {
                // const filePath = path.relative(targetFileName, file);
                const moduleName = utils.getPathName(file);
                paths.push(p);
                pathNames.push(moduleName);
            }
        }
    }
    return {
        paths: paths,
        pathNames: pathNames,
    }
}

/* eslint-disable */
const fs = require('fs');
const path = require('path');
const glob = require('glob');
const utils = require('./utils');
const config = require('./config');


const sourceFileName = config.pagePath;
const targetFileName = config.pageInitStateFileName;

const finalString = {};
exports.handlePageInitStateWatch = function (event, pathName) {
    if (!hasInitState(pathName)) return;
    // pathName= path.relative(targetFileName, pathName);
    console.log('init-state:', event, pathName);
    const initString = getPageInitString(pathName);
    const scope = getPageInitScope(initString);
    const scopeInit = scope + ': ' + initString;
    if (event === 'add' || event === 'change') {
        finalString[scope] = scopeInit;
        writeAllInitState(finalString, targetFileName);
    }
    if (event === 'unlink') {
        delete finalString[scope];
        writeAllInitState(finalString, targetFileName);
    }
}

exports.generateAllInitState = function () {
    const finalString = {};
    const files = glob.sync(sourceFileName, {ignore: config.routesIgnore});
    if (files && files.length) {
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            if (hasInitState && hasInitState(file)) {
                const initString = getPageInitString(file);
                const scope = getPageInitScope(initString);
                finalString[scope] = scope + ': ' + initString;
            }
        }
    }
    writeAllInitState(finalString, targetFileName);
}

function writeAllInitState(finalString, targetFileName) {
    // 拼接写入文件的内容
    const finalStrings = Object.keys(finalString).map(function (key) {
        return finalString[key];
    })
    const fileString = 'export default {\n    ' + finalStrings.join(',\n    ') + '};';
    fs.writeFileSync(targetFileName, fileString);
}

function hasInitState(file) {
    try { // file 文件有可能不存在，会导致webpack停掉
        const fileStr = fs.readFileSync(file);
        // FIXME 这个判断可能不准确
        return fileStr.indexOf('export const INIT_STATE') > 0;
    } catch (e) {
        return true; // 文件被移除之后，也算他没有INIT_STATE
    }
}

function getPageInitString(filePath) {
    if (!fs.existsSync(filePath)) return {};
    // FIXME 这个算法不准确
    const fileString = fs.readFileSync(filePath, 'utf-8');
    const initStart = fileString.indexOf('export const INIT_STATE = ');
    if (initStart > -1) {
        let initString = fileString.substring(initStart);
        const initEnd = initString.indexOf('};');
        initString = initString.substring(0, initEnd + 1);
        return initString.replace('export const INIT_STATE = ', '');
    }
}

function getPageInitScope(initString) {
    const patt = /scope:[ ]*['"]([^'"]+)['"][,]/gm;
    let isScope = false;
    let block = null;
    while ((block = patt.exec(initString)) !== null) {
        isScope = block[0] && block[1];
        if (isScope) {
            return block[1];
        }
    }
}
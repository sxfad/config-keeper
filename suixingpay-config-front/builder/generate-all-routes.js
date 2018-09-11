/* eslint-disable */
const fs = require('fs');
const path = require('path');
const utils = require('./utils');

const config = require('./config');

const sourceFileName = config.routesFileName;
const targetFileName = config.allRoutesFileName;

const imports = [];
const modules = [];
exports.handleWatchAllRoutes = function (event, pathName) {
    if (!isRoutesFile(pathName)) return;
    // pathName= path.relative(targetFileName, pathName);
    console.log('all-routes:', event, pathName);
    const im = utils.getImportStr(pathName);
    const pn = utils.getModuleName(pathName);
    if (event === 'add') {
        imports.push(im);
        modules.push(pn);
        writeAllRoutes(imports, modules, targetFileName);
    }
    if (event === 'unlink') {
        utils.arrayRemove(imports, im);
        utils.arrayRemove(modules, pn);
        writeAllRoutes(imports, modules, targetFileName);
    }
}

exports.generateAllRoutes = function () {
    const result = utils.getImportsAndModules(sourceFileName, targetFileName);
    const imports = result.imports;
    const modules = result.modules;
    writeAllRoutes(imports, modules, targetFileName);
}

function writeAllRoutes(imports, routesNames, targetFileName) {
    let fileString = imports.join('\n');
    fileString += '\n\nexport default [].concat(\n    ';
    fileString += routesNames.join(',\n    ');
    fileString += '\n);\n';
    fs.writeFileSync(targetFileName, fileString);
}

function isRoutesFile(pathName) {
    return path.basename(pathName) === 'routes.js';
}

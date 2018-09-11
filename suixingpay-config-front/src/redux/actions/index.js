import * as utils from './utils';
import * as page from './page';
import * as demo from './demo';
import * as frame from './frame';

const actions = {
    page,
    utils,
    demo,
    frame,
};

const actionsFunctions = {};
function checkActions(acs) {
    for (let key of Object.keys(acs)) {
        const action = acs[key];
        for (let k of Object.keys(action)) {
            if (actionsFunctions[k]) {
                throw Error(`不予许定义同名的action方法：${key}.${k} 与 ${actionsFunctions[k]._module}.${k} 方法冲突！`);
            } else {
                actionsFunctions[k] = action[k];
                actionsFunctions[k]._module = key;
            }
        }
    }
}

checkActions(actions);
export default actionsFunctions;

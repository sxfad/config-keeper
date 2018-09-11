/**
 * Created by an_wch on 2017/9/11.
 */
import {getTopNodeByNode} from 'sx-ui/utils/tree-utils';
import {getMenuData, getMenuTreeData} from './../commons/index';

export default {
    getUniqueArray(array) {
        var hash = {},
            len = array.length,
            result = [];
        for (var i = 0; i < len; i++) {
            if (!hash[array[i]]) {
                hash[array[i]] = true;
                result.push(array[i]);
            }
        }
        return result;
    },
    getRootUrl() {
        const menuDate = getMenuData();
        const menuTreeDate = getMenuTreeData();
        return getTopNodeByNode(menuTreeDate, menuDate[0]);
    },

    /**
     * 环境权限专用
     * 从数组中删除一个元素，此方法具有副作用，修改了原数组
     * @param {Array} arr 需要操作的数组
     * @param {*} item 要删除的元素，注意：内部是用'==='比对的
     */
    arrayRemove(arr, item) {
        let itemIndex = -1;
        for (let i = 0; i < arr.length; i++) {
            if (arr[i].profile === item.profile) {
                itemIndex = i;
                break;
            }
        }
        if (itemIndex > -1) {
            arr.splice(itemIndex, 1);
        }
    }
}

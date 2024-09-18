/**
 * @desc  [自定义校验规则]
 * @example
 *  import { validateRule } from "@/utils/validateRules";
 *  rules: [
 *     { validator: validateRule.emailValue, trigger: 'blur'}
 *  ]
 */

export const rule = {
    /**
     * 校验 请输入中文、英文、数字包括下划线
     * 名称校验
     */
    validatorNameCn(rule, value, callback) {
        let acount = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/
        if (value && (!(acount).test(value))) {
            callback(new Error('请输入中文、英文、数字包括下划线'))
        } else {
            callback()
        }
    },
    /**
     * 校验 请输入中文、英文、数字包括下划线
     * 名称校验
     */
    validatorKey(rule, value, callback) {
        let acount = /^[A-Z_]+$/
        if (value && (!(acount).test(value))) {
            callback(new Error('请输入大写英文、下划线'))
        } else {
            callback()
        }
    },

    /**
     * 校验首尾空白字符的正则表达式
     *
     */
    checkSpace(rule, value, callback) {
        let longrg = /[^\s]+$/;
        if (!longrg.test(value)) {
            callback(new Error('请输入非空格信息'));
        } else {
            callback();
        }
    },

    /**
     * 校验首尾空白字符的正则表达式
     *
     */
    checkPhone(rule, value, callback) {
        if (!(/^1[3456789]\d{9}$/.test(value))) {
            callback(new Error("手机号码有误，请重填"));
        } else {
            callback();
        }
    },
    // 类似金钱,首位不为0,最多2位小数
    checkNumPot2(rule, value, callback) {
        const reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/
        if (!value) {
            return callback(new Error('请填写数字'))
        } else if (!reg.test(value)) {
            return callback(new Error('请填写数字,最多2位小数'))
        } else {
            callback()
        }
    },
    // 整数
    checkInterNum(rule, value, callback) {
        const reg = /^[0-9]*[1-9][0-9]*$/
        if (!value) {
            return callback(new Error('请填写整数'))
        } else if (!reg.test(value)) {
            return callback(new Error('请输入整数'))
        } else {
            callback()
        }
    },
    // 微信号
    checkWxNum(rule, value, callback) {
        var wxreg = /^[a-zA-Z0-9_-]{5,19}$/
        if (!wxreg.test(value)) {
            return callback(new Error('请填写正确的微信号'))
        } else {
            callback()
        }
    },
    //手机或固话
    checkPhoneTwo(rule, value, callback) {
        const reg = /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/;

        if (value == '' || value == undefined || value == null) {
            callback();
        } else {
            if ((!reg.test(value)) && value != '') {
                callback(new Error('请输入正确的电话号码或者固话号码'));
            } else {
                callback();
            }
        }
    },
    //身份证
    validateIdNo(rule, value, callback) {
        const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if (value == '' || value == undefined || value == null) {
            callback("身份证号码不允许为空");
        } else {
            if ((!reg.test(value)) && value != '') {
                callback(new Error('请输入正确的身份证号码'));
            } else {
                callback();
            }
        }
    },
    // 费率 加 税率
    checkMaxVal(rule, value, callback) {
        if (value == '' || value == null) {
            callback();
        } else {
            if (value < 0 || value > 1 || !Number(value)) {
                callback(new Error('请输入0-1之间的数字'));
            } else {
                callback();
            }
        }

    },
    // 费率 加 税率
    checkNaSuiRen(rule, value, callback) {
        if (value == '' || value == null) {
            callback();
        } else {
            if (value < 0 || value > 100 || !Number(value)) {
                callback(new Error('请输入0-100之间的数字'));
            } else {
                callback();
            }
        }

    },

    checkBalance(rule, value, callback) {
        if (value < 0 || value > 9999999999999 || !Number(value)) {
            callback(new Error('请输入0-9999999999999之间的数字'));
        } else {
            callback();
        }
    },

    //银行账户
    checkBankNum(rule, value, callback) {
        if (value == '' || value == null) {
            callback();
        }
        const reg = /^([1-9]{1})(\d{14}|\d{18})$/;
        if ((!reg.test(value))) {
            callback(new Error('请输入正确的银行卡号'));
        } else {
            callback();
        }
    },

    //密码强度
    checkPassWord(value) {
        // 0： 表示第一个级别 1：表示第二个级别 2：表示第三个级别
        // 3： 表示第四个级别 4：表示第五个级别
        var arr = [], array = [25, 50, 75, 100];
        if (value.length < 6) {//最初级别
            return 0;
        }
        if (/\d/.test(value)) {//如果用户输入的密码 包含了数字
            arr.push(25);
        }
        if (/[a-z]/.test(value)) {//如果用户输入的密码 包含了小写的a到z
            arr.push(50);
        }
        if (/[A-Z]/.test(value)) {//如果用户输入的密码 包含了大写的A到Z
            arr.push(75);
        }
        if (/\W/.test(value)) {//如果是非数字 字母 下划线
            arr.push(100);
        }

        return array[arr.length-1]

    }


}

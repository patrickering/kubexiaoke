/**
 * 日期工具类
 * xiaoke
 * 2018年6月11日 16:58:43
 */

var dateUtils = {}
/**
 * 日期格式转换
 * @param str
 * @param format
 */
dateUtils.format = (str, format) => {
    var time = new Date(str.replace(/-/g, '/'))
    var t = new Date(time)
    var tf = function (i) {
        return (i < 10 ? '0' : '') + i
    }
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function (a) {
        switch (a) {
            case 'yyyy':
                return tf(t.getFullYear())
            case 'MM':
                return tf(t.getMonth() + 1)
            case 'mm':
                return tf(t.getMinutes())
            case 'dd':
                return tf(t.getDate())
            case 'HH':
                return tf(t.getHours())
            case 'ss':
                return tf(t.getSeconds())
        }
    })
}



dateUtils.dateToStr = (date, slipe) => {
    var year = date.getFullYear()
    var month = (date.getMonth() + 1).toString()
    var day = (date.getDate()).toString()
    if (month.length === 1) {
        month = '0' + month
    }
    if (day.length === 1) {
        day = '0' + day
    }
    var dateTime = year + slipe + month + slipe + day
    return dateTime
}

dateUtils.dateTo = (date, slipe) => {
    date = new Date(date)
    var year = date.getFullYear()
    var month = (date.getMonth() + 1).toString()
    var day = (date.getDate()).toString()
    var timeHours = date.getHours().toString()
    var timeMinutes = date.getMinutes().toString()
    var timeSeconds = date.getSeconds().toString()
    if (month.length === 1) {
        month = '0' + month
    }
    if (day.length === 1) {
        day = '0' + day
    }
    var dateTime = year + slipe + month + slipe + day + ' ' + timeHours + ':' + timeMinutes + ':' + timeSeconds
    return dateTime
}
dateUtils.dateTo2 = (date, slipe) => {
    date = new Date(date)
    var year = date.getFullYear()
    var month = (date.getMonth() + 1).toString()
    var day = (date.getDate()).toString()
    if (month.length === 1) {
        month = '0' + month
    }
    if (day.length === 1) {
        day = '0' + day
    }
    var dateTime = year + slipe + month + slipe + day
    return dateTime
}

dateUtils.compareDate=(date1,date2)=>{
    // 时间比较  大于返回true
    return date1.valueOf()>date2.valueOf();
}

dateUtils.timesFun = (timesData) => {
    //如果时间格式是正确的，那下面这一步转化时间格式就可以不用了
    var dateBegin = new Date(timesData.replace(/-/g, "/"));//将-转化为/，使用new Date
    var dateEnd = new Date();//获取当前时间
    var dateDiff = dateEnd.getTime() - dateBegin.getTime();//时间差的毫秒数
    var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000));//计算出相差天数
    var leave1 = dateDiff % (24 * 3600 * 1000)    //计算天数后剩余的毫秒数
    var hours = Math.floor(leave1 / (3600 * 1000))//计算出小时数
    //计算相差分钟数
    var leave2 = leave1 % (3600 * 1000)    //计算小时数后剩余的毫秒数
    var minutes = Math.floor(leave2 / (60 * 1000))//计算相差分钟数
    //计算相差秒数
    var leave3 = leave2 % (60 * 1000)      //计算分钟数后剩余的毫秒数
    var seconds = Math.round(leave3 / 1000);
    var timesString = '';

    if (dayDiff != 0) {
        timesString = dayDiff + '天';
    } if (hours != 0) {
        timesString += hours + '小时';
    }
    if (minutes != 0) {
        timesString += minutes + '分钟';
    }

    return timesString
}

export default dateUtils

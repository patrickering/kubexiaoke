import {
    pageApp,
    getApp,
    stateCount
} from '@/api/kube/app'
import dataUtils from '@/utils/dataUtils';
import appUtils from '@/utils/appUtils';

export default {
    namespaced: true,
    state: {

        appList: []// 应用列表
        , appCount: []// 应用总数
        , dictList: []//应用状态字典
        , app: null//应用
        , podList: []//pod 列表
        , appStateCount: {all: 0, run: 0, error: 0, stop: 0}//应用状态统计
        , appState:null
    },
    actions: {
        /**
         * @description 获取应用列表
         */
        getAppList({state, dispatch}, param = {}) {
            state.appState = param.type
            return new Promise((resolve, reject) => {
                pageApp(param.query).then(async res => {
                    if (res.state) {
                        dataUtils.each(res.data.records, (key, obj) => {
                            var dict = param.dictList.filter(item => item.value == obj.state);
                            if (dict != null && dict.length > 0) {
                                res.data.records[key].dictObj = dict[0]
                            }
                        })
                        state.dictList = param.dictList
                        state.appList = res.data.records
                        state.appCount = res.data.total
                    } else {
                        appUtils.showError(res.msg)
                    }
                    // 结束
                    resolve(res);
                })
                    .catch(err => {
                        reject(err);
                    })
            })
        },
        //修改应用状态
        setAppListState({state, dispatch}, param) {
            //修改列表应用状态
            dataUtils.each(state.appList, (key, obj) => {
                if (obj.id == param.appId) {
                    var dict = state.dictList.filter(item => item.value == param.state);
                    if (dict != null && dict.length > 0) {
                        state.appList[key].dictObj = dict[0]
                        state.appList[key].state = param.state
                    }
                }
            })
            //修改应用状态
            if (state.app != null) {
                if (state.app.id == param.appId) {
                    var dict = state.dictList.filter(item => item.value == param.state);
                    if (dict != null && dict.length > 0) {
                        state.app.dictObj = dict[0]
                    }
                    dispatch('getApp', {id: param.appId, dictList: state.dictList})
                    dispatch('getStateCount', {type: state.app.type})
                }
            }else {
                dispatch('getStateCount', {type: state.appState})
            }

        },
        //修改应用个数
        setAppListNowCount({state, dispatch}, param) {
            //修改列表应用个数
            dataUtils.each(state.appList, (key, obj) => {
                if (obj.id == param.appId) {
                    state.appList[key].nowCount = param.count
                }
            })
            //修改应用个数
            if (state.app != null) {
                if (state.app.id == param.appId) {
                    dispatch('getApp', {id: param.appId, dictList: state.dictList})
                }
            }

        },
        //赋值
        setAppList({state, dispatch}, appList) {
            state.appList = appList;
        },

        /**
         * @description 获取应用
         */
        getApp({state, dispatch}, param = {}) {
            state.app = null
            return new Promise((resolve, reject) => {
                getApp(param.id).then(async res => {
                    if (res.state) {
                        state.dictList = param.dictList
                        var dict = param.dictList.filter(item => item.value == res.data.state);
                        res.data.dictObj = dict[0]
                        state.app = res.data
                        state.podList = [];
                        if (state.app.pods != null && state.app.pods.length > 0) {
                            dataUtils.each(state.app.pods, (key, obj) => {
                                let item = {}
                                item.name = obj.metadata.name
                                item.status = obj.status.phase
                                if(obj.status.conditions!=null&&obj.status.conditions.length>0){
                                    item.conditions = obj.status.conditions[0]
                                }else {
                                    item.conditions = obj.status
                                }
                                item.hostIP = obj.status.hostIP
                                item.podIP = obj.status.podIP
                                item.containers = obj.spec.containers
                                item.containerStatuses = obj.status.containerStatuses
                                state.podList.push(item)
                            })
                        }

                        if (res.data.configList == null) {
                            res.data.configList = []
                            res.data.envConfigList = []
                        } else {
                            res.data.envConfigList = []
                            var configList = []
                            dataUtils.each(res.data.configList, (key, obj) => {
                                if (obj.config.type == 0) {
                                    res.data.envConfigList.push({
                                        versionId: [obj.version.configId, obj.versionId]
                                    })
                                } else if (obj.config.type == 1) {
                                    obj.versionId = [obj.version.configId, obj.versionId]
                                    configList.push(obj)
                                }
                            })
                            res.data.configList = configList
                        }


                        if (res.data.type == 'StatefulSet') {
                            let headlessList = []
                            let serviceList = []
                            dataUtils.each(res.data.serviceList, (key, obj) => {
                                if (obj.type == 'Service') {
                                    serviceList.push(obj)
                                } else {
                                    headlessList.push(obj)
                                }
                            })
                            res.data.serviceList = serviceList
                            res.data.headlessList = headlessList
                            if (headlessList.length <= 0) {
                                res.data.headlessList = [{
                                    type: 'Headless',
                                    sign: null,
                                    portList: [{
                                        agreement: 'TCP',
                                        container: null
                                    }]
                                }]
                            }
                        }

                    } else {
                        appUtils.showError(res.msg)
                    }
                    // 结束
                    resolve(res);
                })
                    .catch(err => {
                        // console.log('err: ', err);
                        reject(err);
                    })
            })
        },

        /**
         * @description 应用状态统计
         */
        getStateCount({state, dispatch}, param = {}) {
            return new Promise((resolve, reject) => {
                stateCount(param).then(async res => {
                    if (res.state) {
                        state.appStateCount.all = 0
                        state.appStateCount.run = 0
                        state.appStateCount.stop = 0
                        dataUtils.each(res.data, (key, obj) => {
                            state.appStateCount.all += obj.count
                            if (obj.state == 20) {
                                state.appStateCount.run += obj.count
                            } else if (obj.state == 60) {
                                state.appStateCount.error = obj.count
                            } else if (obj.state == 40) {
                                state.appStateCount.stop = obj.count
                            }
                        })
                        //if(res.data.)

                    } else {
                        appUtils.showError(res.msg)
                    }
                    // 结束
                    resolve(res);
                })
                    .catch(err => {
                        // console.log('err: ', err);
                        reject(err);
                    })
            })
        },
    }
};

<template>
    <div style="display: inline-block" v-if="namespaceInfo!=null">
        <div style="width: 1000px;height: 70px">
            <Row>
                <Col span="5">
                    <div style="display: flex;align-items: center;justify-content: right;">
                        <div :style="{'background-color':'rgba(31,142,207,0.2)'}" style="width: 100px;height: 46px;line-height: 46px;display: flex;align-items: center;justify-content: center;padding: 10px;border-radius: 6px;cursor: pointer;margin-left: 10px">
                            <img  src="@/assets/images/header-namespace-icon-select.png" width="22" height="22" alt="">
                            <div :style="{color:'#1F8ECF'}" style="font-size: 12px;margin-left: 8px">命名空间</div>
                        </div>
                    </div>
                </Col>

                <!--命名空间-->
                <Col span="4" style="padding-left: 20px">
                    <div style="line-height: 24px">
                        <span style="font-size: 12px;color: #7C7C7C">名称</span>
                        <span style="font-size: 12px;color: #212325;margin-left: 10px" v-text="namespaceInfo.name"></span>
                    </div>
                    <div style="line-height: 24px">
                        <span style="font-size: 12px;color: #7C7C7C">标识</span>
                        <span style="font-size: 12px;color: #212325;margin-left: 10px" v-text="namespaceInfo.sign"></span>
                    </div>
                </Col>
                <Col span="7" style="padding-left: 20px">
                    <div style="line-height: 24px;display: flex;align-items: center;">
                        <span style="font-size: 12px;color: #7C7C7C">CPU请求</span>
                        <Progress  style="width: 200px;margin-left: 10px" :stroke-color="['#3DC0A2', '#3DC0A2']" :stroke-width="6" :percent="percentage(namespaceInfo.nowCpuRequest,namespaceInfo.cpuRequest)" />
                    </div>
                    <div style="line-height: 24px;display: flex;align-items: center;">
                        <span style="font-size: 12px;color: #7C7C7C">CPU上限</span>
                        <Progress  style="width: 200px;margin-left: 10px" :stroke-color="['#F78D7E', '#F78D7E']" :stroke-width="6" :percent="percentage(namespaceInfo.nowCpuLimit,namespaceInfo.cpuLimit)" />
                    </div>
                </Col>
                <Col span="7" style="padding-left: 20px">
                    <div style="line-height: 24px;display: flex;align-items: center;">
                        <span style="font-size: 12px;color: #7C7C7C">内存请求</span>
                        <Progress  style="width: 200px;margin-left: 10px" :stroke-color="['#3DC0A2', '#3DC0A2']" :stroke-width="6" :percent="percentage(namespaceInfo.nowMemoryRequest,namespaceInfo.memoryRequest)" />
                    </div>
                    <div style="line-height: 24px;display: flex;align-items: center;">
                        <span style="font-size: 12px;color: #7C7C7C">内存上限</span>
                        <Progress  style="width: 200px;margin-left: 10px" :stroke-color="['#F78D7E', '#F78D7E']" :stroke-width="6" :percent="percentage(namespaceInfo.nowMemoryLimit,namespaceInfo.memoryLimit)" />
                    </div>
                </Col>
            </Row>
        </div>
    </div>
</template>
<script>
    import Setting from '@/setting';
    import dataUtils from '@/utils/dataUtils';
    export default {
        name: 'iHeaderNamespace',
        computed: {

        },
        data(){
            return {
                changeFlag:true,
                namespaceInfo: null//命名空间
            }
        },
        methods: {
            //计算百分比
            percentage(args, args1) {
                if (args == null || args1 == null || args1 == 0) {
                    return 0;
                }
                return Math.floor((args / args1) * 100)
            }
        },
        mounted(){
            this.namespaceInfo = dataUtils.getData(Setting.key.namespaceInfo)
            this.changeFlag = true
        },
        beforeDestroy () {
            this.changeFlag = false
        }
    }
</script>

<template>
    <div class="console-log" style="">
        <Card shadow style="background-color:#353650;position: relative">
            <div slot="title"
                 :style="{'background-color':theme=='black'?'#4a4b6e':'#f6f6f6','color':theme=='black'?'#fff':'#f6f6f6'}"
                 style="text-indent: 10px;">正在获取...
            </div>
            <div slot="extra" style="color: #fff">
                自动滚动：<Switch v-model="openAuto" @on-change="changeAuto" />
                <el-button size="mini" round style="margin-left: 20px" @click="clear">清除日志</el-button>
            </div>
            <div style="height:600px;overflow: auto" ref="scroll" :id="'console-box'+_uid">
                <ul>
                    <li v-for="(item,index) in value" style="color: #ccc">
                        <Row>
                            <Col style="background-color: #4a4b6e;text-align: right;padding-right: 5px;font-size: 16px;line-height: 30px"
                                 span="1" v-text="index"></Col>
                            <Col class="logContent" span="23"
                                 v-html="item"></Col>
                        </Row>
                    </li>
                </ul>
            </div>
            <div style="position: absolute;right: 10px;bottom: 10px;z-index: 10000000000000000000">
                <div @click="handleScroll('top')"
                     style="width: 30px;height: 30px;background-color: #828282;text-align: center;line-height: 30px;border-radius: 3px;color: #fff;cursor: pointer">
                    <Icon type="md-arrow-round-up"/>
                </div>
                <div @click="handleScroll('bottom')"
                     style="width: 30px;height: 30px;background-color: #828282;text-align: center;line-height: 30px;border-radius: 3px;color: #fff;margin-top: 10px;cursor: pointer">
                    <Icon type="md-arrow-round-down"/>
                </div>
            </div>
        </Card>
    </div>
</template>
<script>
    import $ from 'jquery'
    export default {
        props: {
            theme: {
                type: String,
                default: 'black'
            },
            value: {
                type: Array,
                default: []
            }
        },
        name: 'console-log',
        data() {
            return {
                logList: this.value,  //定义一下
                time: 2000,
                timer:null,
                openAuto:false,
                load:true
            }
        },
        watch: {
            value(){
                /*if(this.value.length>8000){
                    this.clear()
                }*/
            }
        },
        methods: {
            handleScroll(type) {
                if (type == 'top') {
                    this.$ScrollTop(this.$refs.scroll, {
                        to: 0,
                        time: this.time
                    })
                    this.openAuto = false
                    this.stopBottom()
                } else {
                    var ele = document.getElementById("console-box"+this._uid);
                    if(ele!=null){
                        this.$ScrollTop(this.$refs.scroll, {
                            to: ele.scrollHeight,
                            time: this.time
                        })
                    }
                    this.openAuto = true
                    this.startBottom()
                }
            },
            startBottom(){
                if(this.timer==null){
                    this.timer = setInterval( ()=> {
                        this.handleScroll('bottom')
                    }, 500);
                }
            },
            stopBottom(){
                clearInterval(this.timer);
                this.timer = null
            },
            scrollFunc(e) {
                e = e || window.event;
                if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件
                    if (e.wheelDelta > 0) { //当滑轮向上滚动时
                        this.openAuto = false
                        this.stopBottom()
                    }
                } else if (e.detail) {  //Firefox滑轮事件
                    if (e.detail > 0) { //当滑轮向上滚动时
                        this.openAuto = false
                        this.stopBottom()
                    }
                }
            },
            changeAuto(val){
                if(val){
                    this.startBottom()
                }else {
                    this.stopBottom()
                }
            },
            clear(){
                this.logList = []
                this.$emit('input', this.logList)
            }
        },
        mounted() {
            let that = this
            //给页面绑定滑轮滚动事件
            var ele = document.getElementById("console-box"+this._uid);
            if (ele.addEventListener) {//firefox
                ele.addEventListener('DOMMouseScroll', this.scrollFunc, false);
            }
            //滚动滑轮触发scrollFunc方法  //ie 谷歌
            ele.onmousewheel = this.scrollFunc;

            $("#console-box"+this._uid).scroll(function(){
                var $this =$(this),
                    viewH =$(this).height(),//可见高度
                    contentH =$(this).get(0).scrollHeight,//内容高度
                    scrollTop =$(this).scrollTop();//滚动高度
                if(contentH <= viewH + scrollTop) { //当滚动到底部时，
                    if(that.load){
                        that.load = false
                        that.$emit('scroll-bottom')
                        //防止重复触发
                        setTimeout(()=>{
                            that.load = true
                        },1000)
                    }

                }
            });
        }

    }
</script>
<style lang="less">
    .console-log .ivu-card-head {
        border-bottom: 0px;
        padding: 0px;
        line-height: 45px;
    }

    .console-log .ivu-card-body {
        padding: 0px;
    }

    .console-log ::-webkit-scrollbar {
        width: 7px;
        height: 10px
    }

    .console-log ::-webkit-scrollbar-track-piece {

        -webkit-border-radius: 0;
        background-color: #353650;
    }

    .console-log ::-webkit-scrollbar-button:vertical {
        display: none;
    }

    .console-log ::-webkit-scrollbar-track, ::-webkit-scrollbar-corner {
        background-color: #353650;
    }

    .console-log ::-webkit-scrollbar-thumb {
        border-radius: 5px;
        background-color: rgba(100, 100, 100, 0.9);
    }

    .console-log ::-webkit-scrollbar-thumb:vertical:hover {
        background-color: rgba(100, 100, 100, 0.95);
    }

    .console-log ::-webkit-scrollbar-thumb:vertical:active {
        background-color: rgba(100, 100, 100, 0.98);
    }
    .logContent{
        background-color: #353650;padding-left: 10px;line-height: 30px;
    }
    .logContent:hover{
        background-color: #4a4b6e;
    }
    .console-log .ivu-card-extra {
        top: 9px;
    }
</style>

<!--
   用户表
   author： chendengwen
   date： 2020-07-18 13:00:20
-->
<template>
    <div>

    </div>
</template>
<script>
    import { delToken } from '@/api/system/token'
    import appUtils from '@/utils/appUtils';

    export default {
        props: {
            //扩展数据
            extend: {
                type: Object
            }
        },
        data() {
            return {

            }
        },
        computed: {},
        methods: {
            /**
             * 事件
             * @param type 事件类型
             * @param data 事件数据
             */
            handleEvent(type, data) {
                switch (type) {
                    case 'del':
                        //删除
                        this.rowDel(data)
                        break;
                }
            },
            /**
             * 删除数据
             */
            rowDel (row) {
                console.log(row)
                let loading = appUtils.showLoading('正在删除...')
                delToken(row.token).then((res) => {
                    appUtils.hideLoading(loading)
                    if (res.state) {
                        this.refresh(this.page)
                        appUtils.showSuccess(res.msg)
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(function () {
                    appUtils.hideLoading(loading)
                })
            },
            /**
             * 刷新数据
             */
            refresh() {
                this.$emit('on-refresh');
            }
        },
        mounted() {
        }
    }
</script>

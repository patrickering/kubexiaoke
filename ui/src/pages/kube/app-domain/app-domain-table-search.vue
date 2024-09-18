<!--
   应用域名
   author： xiaoke
   date： 2024-08-19 00:39:43
-->

<template>
    <div style="display: flex;align-items: center;">
        <Input v-model="appDomainForm.domainPrefix" size="large" clearable placeholder="请输入域名前缀"/>
        <Select v-model="appDomainForm.domainId" size="large" placeholder="请选择域名ID" transfer clearable filterable style="margin-left: 10px">
            <Option v-for="item in extend.domainList" :key="item.id" :value="item.id" :label="item.domain"></Option>
        </Select>
        <Button type="primary" @click="handleSubmit" size="large" style="margin-left: 10px">查询</Button>
        <Button style="margin-left: 8px" @click="handleReset" size="large">重置</Button>
    </div>
</template>
<script>
    import {mapState} from 'vuex';

    export default {
        props: {
            value: {
                type: Object
            },
            //扩展数据
            extend:{
                type: Object
            }
        },
        data() {
            return {
                grid: {
                    xl: 8,
                    lg: 8,
                    md: 12,
                    sm: 24,
                    xs: 24
                },
                //查询表单
                appDomainForm: this.value,
                rules: {}
            }
        },
        computed: {
            ...mapState('admin/layout', [
                'isMobile'
            ]),
            labelWidth() {
                return this.isMobile ? undefined : 100;
            },
            labelPosition() {
                return this.isMobile ? 'top' : 'right';
            }
        },
        methods: {
            /**
             * 执行查询
             */
            handleSubmit() {
                this.$emit('input', this.appDomainForm)
                this.$emit('on-submit');
            },
            /**
             * 重置查询条件
             */
            handleReset() {
                this.$refs.appDomainForm.resetFields();
                this.$emit('input', this.appDomainForm)
                this.$emit('on-reset');
            }
        },
        mounted() {
        }
    }
</script>

<!--
   租户
   author： 超级管理员
   date： 2022-07-30 10:09:28
-->

<template>
    <Form ref="tenantForm" :model="tenantForm" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
        <Row :gutter="24" type="flex">
            <Col v-bind="grid">
                <FormItem label="名称：" prop="name" label-for="name">
                    <Input v-model="tenantForm.name" clearable  placeholder="请输入名称" ></Input>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem label="是否启用：" prop="enable" label-for="enable">
                    <Select v-model="tenantForm.enable" clearable placeholder="请选择是否启用">
                        <Option v-for="item in extend.enableList" :key="item.value" :value="item.value" :label="item.label"></Option>
                    </Select>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem :label-width="0">
                    <Button type="primary" @click="handleSubmit">查询</Button>
                    <Button style="margin-left: 8px" @click="handleReset">重置</Button>
                </FormItem>
            </Col>
        </Row>
    </Form>
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
                tenantForm: this.value,
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
                this.$emit('input', this.tenantForm)
                this.$emit('on-submit');
            },
            /**
             * 重置查询条件
             */
            handleReset() {
                this.$refs.tenantForm.resetFields();
                this.$emit('input', this.tenantForm)
                this.$emit('on-reset');
            }
        },
        mounted() {
        }
    }
</script>

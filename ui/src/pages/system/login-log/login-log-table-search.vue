<!--
   登录日志
   author： xiaoke
   date： 2022-01-23 15:30:20
-->

<template>
    <Form class="searchForm" ref="loginLogForm" :model="loginLogForm" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
        <Row :gutter="24" type="flex">
            <Col v-bind="grid">
                <FormItem label="类型：" prop="type" label-for="type">
                    <Select v-model="loginLogForm.type"  clearable filterable>
                        <Option v-for="item in extend.loginTypeList" :key="item.value" :value="item.value" :label="item.label"></Option>
                    </Select>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem label="IP：" prop="ip" label-for="ip">
                    <Input v-model="loginLogForm.ip" placeholder="请输入IP" clearable   ></Input>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem label="来源：" prop="source" label-for="source">
                    <Input v-model="loginLogForm.source" placeholder="请输入来源" clearable   ></Input>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem label="Token：" prop="source" label-for="source">
                    <Input v-model="loginLogForm.token" placeholder="请输入Token" clearable   ></Input>
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
                    xl: 4,
                    lg: 6,
                    md: 12,
                    sm: 24,
                    xs: 24
                },

                loginLogForm: this.value,
                rules: {}
            }
        },
        computed: {
            ...mapState('admin/layout', [
                'isMobile'
            ]),
            labelWidth() {
                return this.isMobile ? undefined : 80;
            },
            labelPosition() {
                return this.isMobile ? 'top' : 'right';
            }
        },
        methods: {
            handleSubmit() {
                this.$emit('input', this.loginLogForm)
                this.$emit('on-submit');
            },
            handleReset() {
                this.$refs.loginLogForm.resetFields();
                this.$emit('input', this.loginLogForm)
                this.$emit('on-reset');
            }
        },
        mounted() {
        }
    }
</script>

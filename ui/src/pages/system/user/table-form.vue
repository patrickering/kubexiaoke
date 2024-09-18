<!--
   用户表
   author： chendengwen
   date： 2020-07-18 13:00:20
-->
<template>
    <Form ref="form" :model="data" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
        <Row :gutter="24" type="flex">
            <Col v-bind="grid">
                <FormItem label="用户名：" prop="username" label-for="username">
                    <Input v-model="data.username" size="large" clearable  placeholder="请输入用户名" element-id="username"/>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem label="姓名：" prop="name" label-for="name">
                    <Input v-model="data.name" size="large" clearable  placeholder="请输入姓名" element-id="name"/>
                </FormItem>
            </Col>

            <Col v-bind="grid">
                <FormItem label="角色：" prop="roleId" label-for="roleId">
                    <Select v-model="data.roleId" size="large" placeholder="请选择角色" clearable>
                        <Option v-for="item in extend.roleList" :value="item.roleId" :key="item.roleId">{{ item.roleName }}</Option>
                    </Select>
                </FormItem>
            </Col>
            <!-- 此处为展开内容 -->
            <Col v-bind="grid" v-show="collapse">
                <FormItem label="手机：" prop="phone"  label-for="phone">
                    <Input v-model="data.phone" clearable size="large"  placeholder="请输入手机" element-id="phone"/>
                </FormItem>
            </Col>
            <Col v-bind="grid" v-show="collapse">
                <FormItem label="邮箱：" prop="email" label-for="email">
                    <Input v-model="data.email" clearable size="large"  placeholder="请输入邮箱" element-id="email"/>
                </FormItem>
            </Col>
            <Col v-bind="grid" v-show="collapse">
                <FormItem label="账号状态：" prop="loginFlag" label-for="loginFlag">
                    <Select v-model="data.loginFlag" size="large" placeholder="请选择状态" clearable>
                        <Option v-for="item in extend.statusList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                    </Select>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem :label-width="0">
                    <Button type="primary" size="large" @click="handleSubmit">查询</Button>
                    <Button style="margin-left: 8px" size="large" @click="handleReset">重置</Button>
                    <a style="font-size: 14px;margin-left: 8px" @click="collapse = !collapse">
                        <template v-if="!collapse">
                            展开 <Icon type="ios-arrow-down" />
                        </template>
                        <template v-else>
                            收起 <Icon type="ios-arrow-up" />
                        </template>
                    </a>
                </FormItem>
            </Col>
        </Row>

        <Row :gutter="24" type="flex" justify="end">

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
                    xl: 6,
                    lg: 6,
                    md: 12,
                    sm: 24,
                    xs: 24
                },
                collapse: false,
                collapse1: false,
                data: this.value,
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
            handleSubmit() {
                this.$emit('input', this.data)
                this.$emit('on-submit');
            },
            handleReset() {
                this.$refs.form.resetFields();
                this.$emit('input', this.data)
                this.$emit('on-reset');
            }
        },
        mounted() {
        }
    }
</script>

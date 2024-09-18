<template>
    <Form ref="form" :model="data" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
        <Row :gutter="24" type="flex">
            <Col v-bind="grid">
                <FormItem label="名称：" prop="publicName" label-for="publicName">
                    <Input v-model="data.publicName" placeholder="请输入名称" element-id="publicName"/>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem label="键：" prop="publicKey" label-for="publicKey">
                    <Input v-model="data.publicKey" placeholder="请输入键" element-id="publicKey"/>
                </FormItem>
            </Col>
            <Col v-bind="grid">
                <FormItem label="类型：" prop="system" label-for="system">
                    <Select v-model="data.system" placeholder="请选择类型" element-id="system">
                        <Option v-for="item in extend.dictTypeList" :key="item.value" :value="item.value">{{item.label}}</Option>
                    </Select>
                </FormItem>
            </Col>
            <template v-if="collapse">
                <Col v-bind="grid">
                    <FormItem label="状态：" prop="status" label-for="status">
                        <Select v-model="data.status" placeholder="请选择状态" element-id="status">
                            <Option v-for="item in extend.statusTypeList" :key="item.value" :value="item.value">{{item.label}}</Option>
                        </Select>
                    </FormItem>
                </Col>
            </template>
            <Col v-bind="grid">
                <FormItem :label-width="0">
                    <Button type="primary" @click="handleSubmit">查询</Button>
                    <Button style="margin-left: 8px" @click="handleReset">重置</Button>
                    <a style="font-size: 14px;margin-left: 8px" @click="collapse = !collapse">
                        <template v-if="!collapse">
                            展开
                            <Icon type="ios-arrow-down"/>
                        </template>
                        <template v-else>
                            收起
                            <Icon type="ios-arrow-up"/>
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
                    lg: 8,
                    md: 12,
                    sm: 24,
                    xs: 24
                },
                collapse: false,
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

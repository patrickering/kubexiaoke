<!--
   异常
   author： xiaoke
   date： 2020-08-24 21:37:54
-->

<template>
   <Form ref="form" :model="data" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
      <Row :gutter="24" type="flex">
         <Col v-bind="grid">
            <FormItem label="类型：" prop="type" label-for="type">
               <Select v-model="data.type" placeholder="请选择类型" clearable filterable>
                  <Option v-for="item in extend.errorTypeList" :key="item.value" :value="item.value">{{item.label}}</Option>
              </Select>
            </FormItem>
         </Col>
         <Col v-bind="grid">
            <FormItem label="路径：" prop="path" label-for="path">
               <Input v-model="data.path" clearable   placeholder="请输入路径"></Input>
            </FormItem>
         </Col>
         <Col v-bind="grid">
            <FormItem :label-width="0">
               <Button type="primary" @click="handleSubmit">查询</Button>
               <Button style="margin-left: 8px" @click="handleReset">重置</Button>
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
               xl: 8,
               lg: 8,
               md: 12,
               sm: 24,
               xs: 24
            },
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

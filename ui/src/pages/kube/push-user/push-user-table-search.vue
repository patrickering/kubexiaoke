<!--
   推送接收人
   author： xiaoke
   date： 2024-08-28 00:33:59
-->

<template>
   <Form ref="pushUserForm" :model="pushUserForm" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
      <Row :gutter="24" type="flex">
         <Col v-bind="grid">
            <FormItem label="命名空间：" prop="namespace" label-for="namespace">
               <Input v-model="pushUserForm.namespace" clearable  placeholder="请输入命名空间" ></Input>
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
            pushUserForm: this.value,
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
            this.$emit('input', this.pushUserForm)
            this.$emit('on-submit');
         },
         /**
          * 重置查询条件
          */
         handleReset() {
            this.$refs.pushUserForm.resetFields();
            this.$emit('input', this.pushUserForm)
            this.$emit('on-reset');
         }
      },
      mounted() {
      }
   }
</script>

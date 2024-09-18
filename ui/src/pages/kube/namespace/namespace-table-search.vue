<!--
   命名空间
   author： xiaoke
   date： 2024-08-03 15:00:34
-->

<template>
   <Form ref="namespaceForm" :model="namespaceForm" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
      <Row :gutter="24" type="flex">
         <Col v-bind="grid">
            <FormItem label="项目id：" prop="projectId" label-for="projectId">
               <InputNumber style="width: 100%;" v-model="namespaceForm.projectId"      placeholder="请输入项目id"></InputNumber>
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
            namespaceForm: this.value,
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
            this.$emit('input', this.namespaceForm)
            this.$emit('on-submit');
         },
         /**
          * 重置查询条件
          */
         handleReset() {
            this.$refs.namespaceForm.resetFields();
            this.$emit('input', this.namespaceForm)
            this.$emit('on-reset');
         }
      },
      mounted() {
      }
   }
</script>

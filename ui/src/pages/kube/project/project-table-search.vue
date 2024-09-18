<!--
   项目
   author： xiaoke
   date： 2024-08-03 14:21:50
-->

<template>
   <div>
      <Row>
         <Col span="12">
            <div style="color: #202224;font-size: 32px;font-weight: 600">
               所有项目
            </div>
         </Col>
         <Col span="12">22</Col>
      </Row>
      <Form ref="projectForm" :model="projectForm" :rules="rules" :label-width="labelWidth" :label-position="labelPosition">
         <Row :gutter="24" type="flex">
            <Col v-bind="grid">
               <FormItem label="标题：" prop="title" label-for="title">
                  <Input v-model="projectForm.title" clearable  placeholder="请输入标题" ></Input>
               </FormItem>
            </Col>
            <Col v-bind="grid">
               <FormItem label="描述：" prop="remark" label-for="remark">
                  <Input v-model="projectForm.remark" clearable  placeholder="请输入描述" ></Input>
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
            projectForm: this.value,
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
            this.$emit('input', this.projectForm)
            this.$emit('on-submit');
         },
         /**
          * 重置查询条件
          */
         handleReset() {
            this.$refs.projectForm.resetFields();
            this.$emit('input', this.projectForm)
            this.$emit('on-reset');
         }
      },
      mounted() {
      }
   }
</script>

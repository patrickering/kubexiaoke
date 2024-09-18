<!--
   异常记录
   author： xiaoke
   date： 2024-08-22 23:06:02
-->

<template>
   <div style="display: flex;align-items: center;">
      <Input v-model="errorRecordForm.title" size="large" clearable  placeholder="请输入标题" ></Input>
      <Button type="primary" size="large" @click="handleSubmit" style="margin-left: 5px">查询</Button>
      <Button style="margin-left: 8px" size="large" @click="handleReset">重置</Button>
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
            errorRecordForm: this.value,
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
            this.$emit('input', this.errorRecordForm)
            this.$emit('on-submit');
         },
         /**
          * 重置查询条件
          */
         handleReset() {
            this.$refs.errorRecordForm.resetFields();
            this.$emit('input', this.errorRecordForm)
            this.$emit('on-reset');
         }
      },
      mounted() {
      }
   }
</script>

<!--
   配置
   author： xiaoke
   date： 2024-08-03 16:10:50
-->

<template>
   <div style="display: flex;align-items: center;">
      <Input v-model="configForm.name" size="large" clearable  placeholder="请输入名称" ></Input>
      <Input v-model="configForm.type" size="large" clearable  placeholder="请输入类型" style="margin-left: 10px"></Input>
      <Button type="primary" size="large" @click="handleSubmit" style="margin-left: 10px">查询</Button>
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
            configForm: this.value,
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
            this.$emit('input', this.configForm)
            this.$emit('on-submit');
         },
         /**
          * 重置查询条件
          */
         handleReset() {
            this.$refs.configForm.resetFields();
            this.$emit('input', this.configForm)
            this.$emit('on-reset');
         }
      },
      mounted() {
      }
   }
</script>

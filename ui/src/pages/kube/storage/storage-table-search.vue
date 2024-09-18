<!--
   存储
   author： xiaoke
   date： 2024-08-03 17:00:02
-->

<template>
   <div style="display: flex;align-items: center;">
      <Input v-model="storageForm.name" size="large" clearable  placeholder="请输入名称" ></Input>
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
            //查询表单
            storageForm: this.value,
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
            this.$emit('input', this.storageForm)
            this.$emit('on-submit');
         },
         /**
          * 重置查询条件
          */
         handleReset() {
            this.$refs.storageForm.resetFields();
            this.$emit('input', this.storageForm)
            this.$emit('on-reset');
         }
      },
      mounted() {
      }
   }
</script>

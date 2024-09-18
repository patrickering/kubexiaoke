<!--
   应用
   author： xiaoke
   date： 2024-08-03 15:34:01
-->

<template>
   <div style="display: flex;align-items: center;">
      <Select v-model="appForm.state" size="large" placeholder="请选择状态" transfer clearable filterable>
         <Option v-for="item in extend.appStateList" :key="item.value" :value="item.value" :label="item.label"></Option>
      </Select>
      <Input v-model="appForm.name" size="large" style="margin-left: 10px"  clearable  placeholder="请输入名称" ></Input>

      <Button type="primary" size="large" @click="handleSubmit" style="margin-left: 10px">查询</Button>
      <Button size="large" @click="handleReset" style="margin-left: 8px">重置</Button>
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
            appForm: this.value,
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
            this.$emit('input', this.appForm)
            this.$emit('on-submit');
         },
         /**
          * 重置查询条件
          */
         handleReset() {
            this.$refs.appForm.resetFields();
            this.$emit('input', this.appForm)
            this.$emit('on-reset');
         }
      },
      mounted() {
      }
   }
</script>

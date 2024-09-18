<!--
   用户表
   author： chendengwen
   date： 2020-07-18 13:00:20
-->
<template>
    <div></div>
</template>
<script>
    import {mapState} from 'vuex';

    export default {
        props: {
            value: {
                type: Object
            },
            //扩展数据
            extend: {
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

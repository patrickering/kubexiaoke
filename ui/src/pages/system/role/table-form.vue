<template>
    <div style="display: flex;align-items: center;">
        <Input v-model="data.name" size="large" placeholder="请输入角色名称" element-id="name" />
        <Button type="primary" size="large" @click="handleSubmit" style="margin-left: 10px">查询</Button>
        <Button style="margin-left: 8px" size="large" @click="handleReset">重置</Button>
    </div>
</template>
<script>
    import { mapState } from 'vuex';

    export default {
        data () {
            return {
                grid: {
                    xl: 8,
                    lg: 8,
                    md: 12,
                    sm: 24,
                    xs: 24
                },
                collapse: false,
                data: {
                    name: null
                },
                rules: {

                }
            }
        },
        computed: {
            ...mapState('admin/layout', [
                'isMobile'
            ]),
            labelWidth () {
                return this.isMobile ? undefined : 100;
            },
            labelPosition () {
                return this.isMobile ? 'top' : 'right';
            }
        },
        methods: {
            handleSubmit () {
                this.$emit('on-submit', this.data);
            },
            handleReset () {
                this.$refs.form.resetFields();
                this.$emit('on-reset',this.data);
            },
            handleAdd () {
                this.$emit('on-add');
            }
        }
    }
</script>

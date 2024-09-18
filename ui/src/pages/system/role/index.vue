<!--
   系统角色表
   author： chensiqi
   date： 2019-08-05 18:05:38
-->
<template>
    <div style="width: 1200px;margin: 0 auto;margin-top: 10px">
        <Card :bordered="false" dis-hover :padding="32" style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);">
            <Row>
                <Col :span="16">
                    <div style="color: #202224;font-size: 24px;font-weight: 600">角色管理</div>
                </Col>
                <Col :span="8">
                    <table-form ref="form" @on-add="edit(null)" @on-submit="search" @on-reset="search" />
                </Col>
            </Row>
            <div style="margin-top: 10px">
                <table-list ref="table" @on-fullscreen="handleFullscreen" />
            </div>
        </Card>
    </div>
</template>
<script>
    import screenfull from 'screenfull';
    import tableForm from './table-form';
    import tableList from './table-list';

    export default {
        name: 'auth-role',
        components: { tableForm,tableList },
        data() {
            return {

            }
        },
        methods: {
            search (data) {
                this.$refs.table.search(data);
            },
            getData () {
                this.$refs.table.getData();
            },
            handleFullscreen (state) {
                if (state) {
                    screenfull.request(this.$refs.card.$el);
                } else {
                    screenfull.exit();
                }
            }
        },
        mounted() {
            this.getData();
        }
    }
</script>

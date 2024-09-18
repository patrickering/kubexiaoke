<template>
    <span class="i-layout-header-trigger" :class="{ 'i-layout-header-trigger-min': true }" @click="handleReload">
        <Icon custom="i-icon i-icon-refresh" />
    </span>
</template>
<script>
    import { mapState } from 'vuex';

    export default {
        name: 'iHeaderReload',
        computed: {
            ...mapState('admin/layout', [
                'isMobile'
            ])
        },
        methods: {
            handleReload () {
                this.$emit('on-reload');
            }
        }
    }
</script>

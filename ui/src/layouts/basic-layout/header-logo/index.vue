<template>
    <i-link class="i-layout-header-logo" :class="{ 'i-layout-header-logo-stick': !isMobile }" to="/">
        <img src="@/assets/images/logo-small.png" v-if="isMobile">
        <img src="@/assets/images/logo_head.png" v-else-if="headerTheme === 'light'">
    </i-link>
</template>
<script>
    import { mapState } from 'vuex';

    export default {
        name: 'iHeaderLogo',
        computed: {
            ...mapState('admin/layout', [
                'isMobile',
                'headerTheme'
            ])
        }
    }
</script>

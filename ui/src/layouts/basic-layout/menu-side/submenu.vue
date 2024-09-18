<template>
    <Submenu :name="menu.path">
        <template slot="title">
            <i-menu-side-title :menu="menu" />
            <Badge class="i-layout-menu-side-badge" v-if="badgeData" v-bind="badgeData" />
        </template>
        <template v-for="(item, index) in menu.children">
            <i-menu-side-item v-if="item.children === undefined || !item.children.length" :menu="item" :key="index" />
            <i-menu-side-submenu v-else :menu="item" :key="index" />
        </template>
    </Submenu>
</template>
<script>
    import iMenuSideItem from './menu-item';
    import iMenuSideTitle from './menu-title';
    import menuBadge from '../mixins/menu-badge';

    export default {
        name: 'iMenuSideSubmenu',
        components: { iMenuSideItem, iMenuSideTitle },
        mixins: [ menuBadge ],
        props: {
            menu: {
                type: Object,
                default () {
                    return {}
                }
            }
        }
    }
</script>

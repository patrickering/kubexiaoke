<template>
    <div style="position: relative">
        <div v-if="activePath==menu.path" style="position: absolute;width: 9px;height: 52px;background-color: #1F8ECF;left: 3px;top: 0px;border-radius: 5px"></div>

        <MenuItem class="menuA" :to="menu.path" :replace="menu.replace" :target="menu.target" :name="menu.path"
                  @click.native="handleClick(menu.path)">
            <i-menu-side-title :menu="menu" :hide-title="hideTitle"/>
            <Badge class="i-layout-menu-side-badge" v-if="badgeData" v-bind="badgeData"/>
        </MenuItem>
    </div>
</template>
<script>
    import iMenuSideTitle from './menu-title';
    import clickItem from '../mixins/click-item';
    import menuBadge from '../mixins/menu-badge';
    import { mapState } from 'vuex';
    export default {
        name: 'iMenuSideItem',
        components: {iMenuSideTitle},
        mixins: [clickItem, menuBadge],
        props: {
            menu: {
                type: Object,
                default() {
                    return {}
                }
            },
            hideTitle: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            ...mapState('admin/menu', [
                'activePath',
                'openNames'
            ]),
        }
    }
</script>
<style>
    .menuA {
        padding-left: 0px !important;
        margin-left: 24px;
        margin-right: 24px;
    }

    .ivu-menu-vertical .ivu-menu-submenu .ivu-menu-item {
        margin-left: 43px;
    }
</style>

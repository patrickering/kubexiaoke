<template>
    <span class="i-layout-menu-side-title">
        <span class="i-layout-menu-side-title-icon" :class="{ 'i-layout-menu-side-title-icon-single': hideTitle }" v-if="menu.icon!=null && menu.icon!=''">
            <Icon v-if="isIcon(menu.icon)" :custom="menu.icon"  />
            <img v-else :src="menu.icon" />
        </span>
        <span :style="{'margin-left':(menu.icon==null || menu.icon=='')?'24px':''}" class="i-layout-menu-side-title-text" :class="{ 'i-layout-menu-side-title-text-selected': selected }" v-if="!hideTitle">{{ tTitle(menu.name) }}</span>
    </span>
</template>
<script>
    import tTitle from '../mixins/translate-title';

    export default {
        name: 'iMenuSideTitle',
        mixins: [ tTitle ],
        props: {
            menu: {
                type: Object,
                default () {
                    return {}
                }
            },
            hideTitle: {
                type: Boolean,
                default: false
            },
            // 用于侧边栏收起 Dropdown 当前高亮
            selected: {
                type: Boolean,
                default: false
            }
        },
        methods: {
            isIcon(icon){
                if(icon.indexOf('i-')==0){
                    return true
                }
                return false
            }
        }
    }
</script>

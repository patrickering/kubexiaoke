<template>
    <span class="i-layout-header-trigger i-layout-header-trigger-min" v-if="user!=null">
        <Dropdown :trigger="isMobile ? 'click' : 'hover'" class="i-layout-header-user" :class="{ 'i-layout-header-user-mobile': isMobile }" @on-click="handleClick">
            <Avatar size="small" :src="user.avatar" @on-error="user.avatar = '/images/defaultAvatar.png'"/>
            <span class="i-layout-header-user-name" v-if="!isMobile">{{ user.name }}</span>
            <DropdownMenu slot="list">
                <i-link to="/setting">
                    <DropdownItem>
                        <span>{{ $t('basicLayout.user.setting') }}</span>
                    </DropdownItem>
                </i-link>
                <DropdownItem divided name="logout">
                    <span>{{ $t('basicLayout.user.logOut') }}</span>
                </DropdownItem>
            </DropdownMenu>
        </Dropdown>
    </span>
</template>
<script>
    import { mapState, mapActions } from 'vuex';
    import dataUtils from "@/utils/dataUtils";
    import Setting from "@/setting";
    export default {
        name: 'iHeaderUser',
        computed: {
            ...mapState('admin/user', [
                'info'
            ]),
            ...mapState('admin/layout', [
                'isMobile',
                'logoutConfirm'
            ])
        },
        data(){
            return {
                user:null
            }
        },
        methods: {
            ...mapActions('admin/account', [
                'logout'
            ]),
            handleClick (name) {
                if (name === 'logout') {
                    this.logout({
                        confirm: this.logoutConfirm,
                        vm: this
                    });
                }
            }
        },
        mounted(){
            this.user = dataUtils.getData(Setting.key.userInfo)
        }
    }
</script>

import { mapState } from 'vuex';

export default {
    computed: {
        ...mapState('admin/menu', [
            'menuBadge'
        ]),
        badgeData () {
            let data = null;
            const menuBadge = this.menuBadge.find(item => item.path === this.menu.path);
            if (menuBadge) data = menuBadge;
            return data;
        }
    }
}

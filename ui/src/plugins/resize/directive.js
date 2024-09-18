import resize from './resize'
const importDirective = Vue => {
    /**
     * Window大小变化回调 v-resize='function'
     */
    Vue.directive('resize', resize)
}

export default importDirective

const resize = {
    inserted: (el, binding, vNode) => {
        // 指令的绑定值，是一个function函数
        const callback = binding.value
        // 延时执行函数的毫秒数
        const debounce = binding.arg || 1000

        // 禁止执行与事件关联的默认动作
        const options = binding.modifiers || {passive: true}

        let debounceTimeout = null
        const onResize = () => {
            clearTimeout(debounceTimeout)
            debounceTimeout = setTimeout(callback, debounce, options)
        }
        // 监听窗口缩放
        window.addEventListener('customResize', onResize, options)
        window.addEventListener('resize', onResize, options)
        // 存储监听窗口缩放事件的参数，方便在unbind钩子函数中解除事件绑定的时候使用到
        el._onResize = {
            onResize,
            options,
        }
    },
    unbind(el, binding) {
        const {onResize, options} = el._onResize
        window.removeEventListener('customResize', onResize, options)
        window.removeEventListener('resize', onResize, options)
        delete el._onResize
    },
}
export default resize

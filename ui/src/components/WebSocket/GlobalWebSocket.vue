<template>

</template>
<script>
    import dataUtils from "../../utils/dataUtils";
    import Setting from "../../setting";
    import appUtils from "../../utils/appUtils";

    export default {
        props: {
            uri: {
                type: String
            },
        },
        data() {
            return {
                webSocket: null, // webSocket实例
                lockReconnect: false, // 重连锁，避免多次重连
                maxReconnect: 6,  // 最大重连次数， -1 标识无限重连
                reconnectTime: 0, // 重连尝试次数
                heartbeat: {
                    interval: 30 * 1000, // 心跳间隔时间
                    timeout: 10 * 1000, // 响应超时时间
                    pingTimeoutObj: null, // 延时发送心跳的定时器
                    pongTimeoutObj: null, // 接收心跳响应的定时器
                    pingMessage: JSON.stringify({type: 'ping'}) // 心跳请求信息
                }
            }
        },
        computed: {},
        created() {
            this.initWebSocket()
        },
        destroyed: function () {
            this.webSocket.close()
            this.clearTimeoutObj(this.heartbeat)
        },
        methods: {
            /**
             * 初始化 weoSocket
             */
            initWebSocket() {
                let namespace = dataUtils.getData(Setting.key.namespace)
                let topic = namespace
                console.log('建立WebSocket连接')
                // ws地址
                let host = window.location.host;
                let wsUri = `ws://${host}${this.uri}?topic=${topic}`
                // 建立连接
                this.webSocket = new WebSocket(wsUri)
                // 连接成功
                this.webSocket.onopen = this.onOpen
                // 连接错误
                this.webSocket.onerror = this.onError
                // 接收信息
                this.webSocket.onmessage = this.onMessage
                // 连接关闭
                this.webSocket.onclose = this.onClose
            },
            /**
             * 重新连接
             */
            reconnect() {
                if (!this.token) {
                    return
                }
                if (this.lockReconnect || (this.maxReconnect !== -1 && this.reconnectTime > this.maxReconnect)) {
                    return
                }
                this.lockReconnect = true
                setTimeout(() => {
                    this.reconnectTime++
                    // 建立新连接
                    this.initWebSocket()
                    this.lockReconnect = false
                }, 5000)
            },
            /**
             * 清空定时器
             */
            clearTimeoutObj: function (heartbeat) {
                heartbeat.pingTimeoutObj && clearTimeout(heartbeat.pingTimeoutObj)
                heartbeat.pongTimeoutObj && clearTimeout(heartbeat.pongTimeoutObj)
            },
            /**
             * 开启心跳
             */
            startHeartbeat() {
                const webSocket = this.webSocket
                const heartbeat = this.heartbeat
                // 清空定时器
                this.clearTimeoutObj(heartbeat)
                // 延时发送下一次心跳
                heartbeat.pingTimeoutObj = setTimeout(() => {
                    // 如果连接正常
                    if (webSocket.readyState === 1) {
                        //这里发送一个心跳，后端收到后，返回一个心跳消息，
                        webSocket.send(heartbeat.pingMessage)
                        // 心跳发送后，如果服务器超时未响应则断开，如果响应了会被重置心跳定时器
                        heartbeat.pongTimeoutObj = setTimeout(() => {
                            webSocket.close()
                        }, heartbeat.timeout)
                    } else {
                        // 否则重连
                        this.reconnect()
                    }
                }, heartbeat.interval)
            },
            /**
             * 连接成功事件
             */
            onOpen() {
                console.log('WebSocket连接成功')
                //开启心跳
                this.startHeartbeat()
                this.reconnectTime = 0
            },
            /**
             * 连接失败事件
             * @param e
             */
            onError(e) {
                console.log(e)
                //错误
                console.log(`WebSocket connection error：${e.code} ${e.reason} ${e.wasClean}`)
                //重连
                this.reconnect()
            },
            /**
             * 连接关闭事件
             * @param e
             */
            onClose(e) {
                //关闭
                console.log(`WebSocket connection closed：${e.code} ${e.reason} ${e.wasClean}`)
                //重连
                this.reconnect()
            },
            /**
             * 接收服务器推送的信息
             * @param msgEvent
             */
            onMessage(msgEvent) {

                //收到服务器信息，心跳重置并发送
                this.startHeartbeat()
                const text = msgEvent.data
                if (text.indexOf('pong') > 0) {
                    return
                }

                let message = JSON.parse(msgEvent.data)
                console.log('接收到消息：')
                console.log(message)
                if (message.type == 'appList') {
                    //this.$store.dispatch('app/app/setAppListState', message.content);
                    this.$EventBus.$emit("app_state",  message.content);
                    if (message.content.state == '20') {
                        appUtils.showSuccess('应用:' + message.content.appName + '启动成功')
                    } else if (message.content.state == '40') {
                        appUtils.showSuccess('应用:' + message.content.appName + '停止成功')
                    } else if (message.content.state == '60') {
                        appUtils.showError('应用:' + message.content.appName + '运行异常')
                    }
                } else if (message.type == 'appCount') {
                    this.$EventBus.$emit("app_count");
                }

            },
            /**
             * 数据发送
             * @param msg
             */
            send(msg) {
                //数据发送
                this.webSocket.send(msg)
            }
        }
    }

</script>

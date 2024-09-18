<template>
    <div>
        <codemirror
                :ref="codeId"
                :value="content"
                :options="cmOptions"
                @changes="changes"
                class="code"
        ></codemirror>
    </div>

</template>
<script>
    import {codemirror} from 'vue-codemirror'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/addon/fold/foldgutter.css'
    import 'codemirror/theme/base16-dark.css'
    import 'codemirror/theme/nord.css'

    require('codemirror/addon/fold/foldcode.js')
    require('codemirror/addon/fold/foldgutter.js')
    require('codemirror/addon/fold/brace-fold.js')
    require('codemirror/addon/fold/indent-fold.js')
    require('codemirror/addon/fold/comment-fold.js')
    require('codemirror/mode/javascript/javascript.js')
    require('codemirror/mode/css/css.js')
    require('codemirror/mode/htmlmixed/htmlmixed.js')

    export default {
        name: 'code-editor',
        components: {
            codemirror
        },
        props: {
            value: {
                type: String,
                default: ''
            },
            mode: {
                type: String,
                default: 'text/html'
            },
            readOnly: {
                type: Boolean,
                default: false
            },
            height: {
                type: Number,
                default: 500
            }
        },
        computed: {
            codemirrorDom() {
                return this.$refs[this.codeId].codemirror
            }
        },
        watch:{
            value(){
                this.$nextTick(()=>{
                    if (this.value == null) {
                        this.content = ''
                    } else {
                        this.content = this.value
                    }
                })
            }
        },
        data() {
            return {
                codeId: 'code-editor-' + +new Date(),
                cmOptions: {
                    //语言
                    mode: 'text/html',
                    //主题
                    theme: 'nord',
                    //显示行号
                    lineNumbers: true,
                    // 软换行
                    lineWrapping: true,
                    // tab宽度
                    tabSize: 8,
                    gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
                    //只读
                    readOnly: this.readOnly
                },
                content: null
            }
        },
        methods: {
            //编辑器内容发生改变
            changes(val) {
                console.log(val.getValue())
                this.$emit('input', val.getValue())
            },
        },
        mounted() {


        }

    }
</script>
<style lang="less">
    .CodeMirror {
        border-radius: 8px;
        height: 500px;
    }

    .cm-s-nord .CodeMirror-linenumber {
        color: #FFFFFF;
        font-weight: 600;
    }
</style>

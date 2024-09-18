<template>
    <div>
        <Select :ref="treeId" v-model="selectData" :placeholder="placeholder" clearable @on-clear="clear">
            <Option v-for="item in list" style="display: none;" :key="item[dataKey]" :value="item[dataKey]"
                    :label="item[defaultProps.label]"></Option>
            <el-tree
                    auto-expand-parent
                    :current-node-key="current"
                    :default-expanded-keys="expanded"
                    :data="data"
                    node-key="id"
                    highlight-current
                    @node-click="nodeClick"
                    :props="defaultProps"
            ></el-tree>
        </Select>
    </div>
</template>

<script>
    import dataUtils from '@/utils/dataUtils';

    export default {
        name: 'select-tree',
        props: {
            value: {},
            data: {
                type: Array
            },
            defaultProps: {
                type: Object,
                default(){
                    return {
                        children: 'children',
                        label: 'name'
                    }
                }
            },
            placeholder: {
                type: String,
                default(){
                    return '请选择'
                }
            },
            dataKey:{
                default(){
                    return 'id'
                }
            },
            last:{
                type: Boolean,
                default(){
                    return false
                }
            },
            site:{
                type: Boolean,
                default(){
                    return false
                }
            }
        },
        computed: {
            list(){
                let list = []
                this.treeToList(this.data,list)
                return list
            },
            treeId() {
                return `selectTree${this._uid}`
            }
        },
        data () {
            return {
                selectData:null,
                expanded:[],
                current:null
            };
        },
        watch: {
            value () {
                this.selectData = this.value
                if(this.value!=null){
                    this.expanded = [this.value]
                }else {
                    this.expanded = []
                }
                this.current = this.value
            }
        },
        methods: {
            treeToList(tree, list){
                dataUtils.each(tree, (key, obj) => {
                    list.push(obj)
                    if (obj[this.defaultProps.children] != null && obj[this.defaultProps.children].length > 0) {
                        this.treeToList(obj[this.defaultProps.children], list)
                    }
                })
            },
            nodeClick(data){
                if(this.site){
                    if(data.infoFlag!=true){
                        return
                    }
                }
                if(this.last){
                    if(data[this.defaultProps.children]==null || data[this.defaultProps.children].length<=0){
                        this.selectData = data[this.dataKey]
                        this.$emit('input', this.selectData)
                        this.$emit('on-change', this.selectData)
                    }
                }else {
                    this.selectData = data[this.dataKey]
                    this.$emit('input', this.selectData)
                    this.$emit('on-change', this.selectData)
                }
                if(data[this.defaultProps.children]==null || data[this.defaultProps.children].length<=0){
                    this.$refs[this.treeId].toggleMenu();
                }

            },
            clear(){
                this.selectData = null
                this.$emit('input', this.selectData)
            }
        },
        mounted () {
            this.selectData = this.value
        }
    };
</script>
<style>

</style>

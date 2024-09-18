<template>
    <div>
      <el-table
          size="mini"
          :data="templateList"
          border
          style="width: 100%;margin-top: 10px">
        <el-table-column
            prop="name"
            label="名称">
        </el-table-column>
        <el-table-column
            prop="createTime"
            align="center"
            label="选择模板"
            width="100">
          <template slot-scope="scope">
            <el-button
                type="text"
                size="mini"
                @click="select(scope.row)">选择
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
</template>
<script>
import {listTemplate} from '@api/kube/template';

    export default {
        props: {
            type: {
                type: Number,
                default: 0
            }
        },
        name: 'system-template',
        data() {
            return {
              templateList: [],  //模板列表
            }
        },
        watch: {

        },
        methods: {
          getData(){
            listTemplate({type:this.type}).then(res=>{
              this.templateList = res.data
            })
          },
          //选择模板
          select(row){
            this.$emit('on-change', row);
          }
        },
        mounted() {
          this.getData()
        }

    }
</script>

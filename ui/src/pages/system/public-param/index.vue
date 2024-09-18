<template>
    <div v-if="publicParam!=null">
        <Card :title="publicParam.publicName" :bordered="true" :dis-hover="true"  :padding="16">
            <Button type="primary" @click="save">保存</Button>
            <UEditor style="margin-top: 10px" v-if="publicParam" v-model="publicParam.publicValue"/>
        </Card>
    </div>
</template>

<script>
    import {getByKey,putObj} from '@/api/system/param'
    import appUtils from '../../../utils/appUtils';
    export default {
        name: "public-param",
        props: {
            paramKey: {
                type: String
            }
        },
        data() {
            return {
                publicParam:null
            };
        },
        methods: {
            getData(){
                this.publicParam = null
                getByKey(this.paramKey).then(res=>{
                    this.publicParam = res.data
                })
            },
            save(){
                let loading = appUtils.showLoading('正在保存....')
                putObj({publicId:this.publicParam.publicId,publicValue:this.publicParam.publicValue,publicKey:this.publicParam.publicKey}).then(res => {
                    appUtils.showSuccess(res.msg)
                    appUtils.hideLoading(loading)
                }).catch((err) => {
                    appUtils.showSuccess(err.msg)
                    appUtils.hideLoading(loading)
                })
            }
        },
        mounted() {
            this.getData()
        }
    };
</script>
<style>

</style>

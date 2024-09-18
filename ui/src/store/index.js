import Vue from 'vue';
import Vuex from 'vuex';

import admin from './modules/admin'
import app from './modules/app'

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        admin,
        app
    }
})

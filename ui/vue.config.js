const Setting = require('./src/setting.env');

// 拼接路径
const resolve = dir => require('path').join(__dirname, dir);

//接口网关地址
const gateway = 'http://127.0.0.1:8089'

// 增加环境变量
process.env.VUE_APP_VERSION = require('./package.json').version;
process.env.VUE_APP_BUILD_TIME = require('dayjs')().format('YYYY-M-D HH:mm:ss');

module.exports = {
    publicPath: Setting.publicPath,
    lintOnSave: Setting.lintOnSave,
    outputDir: Setting.outputDir,
    assetsDir: Setting.assetsDir,
    runtimeCompiler: true,
    productionSourceMap: false,
    devServer: {
        disableHostCheck: true,
        publicPath: Setting.publicPath,
        port: 8070,
        proxy: {
            '/xiaoke-kube/': {
                target: gateway,
                ws: true,
                changeOrigin: true,
                secure: false,
                pathRewrite: {
                    '^/xiaoke-kube': '/xiaoke-kube'
                }
            }
        }
    },
    css: {
        loaderOptions: {
            less: {

            }
        }
    },
    chainWebpack: config => {
        config.plugins
            .delete('prefetch')
            .delete('preload');
        config.resolve
            .symlinks(true);
        config
            .when(process.env.NODE_ENV === 'development',
                  // sourcemap不包含列信息
                  config => config.devtool('cheap-source-map')
            )
            .when(process.env.NODE_ENV !== 'development', config => {

        });
        config.module
            .rule('js')
            .test(/\.jsx?$/)
            .exclude
            .end();
        config.module
            .rule('vue')
            .test(/\.vue$/)
            .use('iview-loader')
            .loader('iview-loader')
            .tap(() => {
                return Setting.iviewLoaderOptions
            })
            .end();
        config.module
            .rule('md')
            .test(/\.md$/)
            .use('text-loader')
            .loader('text-loader')
            .end();
        config.module
            .rule('i18n')
            .resourceQuery(/blockType=i18n/)
            .use('i18n')
            .loader('@kazupon/vue-i18n-loader')
            .end();
        const imagesRule = config.module.rule('images');
        imagesRule
            .test(/\.(png|jpe?g|gif|webp|svg)(\?.*)?$/)
            .exclude
            .add(resolve('src/assets/svg'))
            .end();
        config.resolve.alias
            .set('@api', resolve('src/api'));
        config.node
            .set('__dirname', true)
            .set('__filename', true);
        const entry = config.entry('app');
    }
};

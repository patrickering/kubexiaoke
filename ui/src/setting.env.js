const env = process.env.NODE_ENV;

const Setting = {
    publicPath: env === 'development' ? '/' : '/',
    outputDir: 'dist',
    assetsDir: '',
    lintOnSave: false,
    iviewLoaderOptions: {
        prefix: false
    }
};

module.exports = Setting;

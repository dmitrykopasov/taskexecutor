module.exports = {
    devServer: {
        port: 8081,
        proxy: {
            '/web': {
                target: 'http://217.73.58.38:8080/',
                changeOrigin: true
            }
        }
    },
    outputDir: 'target/dist',
    assetsDir: 'static'
};

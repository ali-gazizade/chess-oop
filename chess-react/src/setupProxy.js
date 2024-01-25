const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    '/bi',  // specify the API path you want to rewrite
    createProxyMiddleware({
      target: 'http://localhost:8080',  // specify the target URL
      changeOrigin: true,
      pathRewrite: {
        '^/bi': '',  // remove the '/api' prefix
      },
    })
  );
};
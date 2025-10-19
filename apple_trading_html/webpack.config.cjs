const path = require('path');
const { VueLoaderPlugin } = require('vue-loader');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  // 入口文件
  entry: './src/main.js',
  // 输出配置（打包后的文件存放目录）
  output: {
    path: path.resolve(__dirname, 'deploy/dist'),
    filename: 'bundle.js',  // 带哈希值的输出文件名，避免缓存
    clean: true  // 每次打包前清空 dist 目录
  },
  // 模块规则（处理不同类型的文件）
  module: {
    rules: [
      // 1. 处理 Vue 单文件组件（.vue）
      {
        test: /\.vue$/,
        loader: 'vue-loader'  // 依赖 vue-loader 和 @vue/compiler-sfc
      },
      // 2. 处理 JavaScript 文件（用 Babel 转换）
      {
        test: /\.js$/,
        exclude: /node_modules/,  // 排除 node_modules 目录（无需转换）
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env'] // 转译 ES6+ 语法为兼容代码
          }
        }
      },
      // 3. 处理 CSS 文件（可选，根据项目需求）
      {
        test: /\.css$/,
        use: [
          'style-loader',  // 将 CSS 注入到 DOM 中
          'css-loader'     // 解析 CSS 文件中的 @import 和 url()
        ]
      }
    ]
  },
  // 插件配置
  plugins: [
    new VueLoaderPlugin(),  // 必须添加，否则 vue-loader 无法正常工作
    // 生成 HTML 入口文件，并自动引入打包后的 JS
    new HtmlWebpackPlugin({
      template: './index.html',  // 模板 HTML
      filename: 'index.html'            // 输出的 HTML 文件名
    })
  ],
  // 开发服务器配置（用于本地开发）
  devServer: {
    static: path.join(__dirname, 'dist'),  // 静态文件目录
    port: 8888,                            // 端口号
    hot: true,                             // 热更新（修改代码后无需刷新页面）
    open: true                             // 启动后自动打开浏览器
  },
  // 模式（开发环境：development，生产环境：production）
  mode: 'development'
};
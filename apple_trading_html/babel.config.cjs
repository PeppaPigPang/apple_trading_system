module.exports = {
  presets: [
    [
      '@babel/preset-env',  // 自动根据目标浏览器转换语法
      {
        targets: {          // 目标浏览器配置（根据项目需求调整）
          browsers: ['last 2 versions', 'ie >= 11']  // 兼容最近 2 个版本浏览器和 IE11
        },
        useBuiltIns: 'usage',  // 自动引入需要的 polyfill（如 Promise、Array.prototype.includes）
        corejs: 3              // 依赖 core-js@3 提供 polyfill（需安装）
      }
    ]
  ]
};
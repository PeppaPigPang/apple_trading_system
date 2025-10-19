# 基础镜像：Nginx
FROM nginx:alpine

# 复制打包后的静态文件到 Nginx 的默认站点目录
# （假设解压后的 deploy 目录下有 dist 文件夹）
COPY dist/ /usr/share/nginx/html/

# 可选：替换 Nginx 配置（比如自定义端口、反向代理）
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露 Nginx 端口（默认 80）
EXPOSE 8888
#!/bin/bash
# ===================== 配置区域 =====================
ZIP_PACKAGE="apple_trading_system_deploy-1.0-SNAPSHOT.zip"  # zip 包文件名
EXTRACT_DIR="apple_deploy_temp"                             # 解压临时目录
DOCKER_IMAGE_NAME="apple-trading-system"                    # Docker 镜像名
DOCKER_CONTAINER_NAME="apple-trading-container"             # Docker 容器名
# ===================================================

# 1. 检查 zip 包是否存在
if [ ! -f "$ZIP_PACKAGE" ]; then
  echo "错误：未找到 zip 包 ${ZIP_PACKAGE}，请确保脚本与 zip 包在同一目录！"
  exit 1
fi

# 2. 解压 zip 包到临时目录
echo "开始解压 ${ZIP_PACKAGE}..."
unzip -q "$ZIP_PACKAGE" -d "$EXTRACT_DIR" || {
  echo "解压失败！请检查 unzip 命令是否安装，或 zip 包是否损坏。"
  exit 1
}
echo "解压完成！"

# 3. 进入 deploy 目录（假设 Dockerfile 在此目录下）
cd "${EXTRACT_DIR}/deploy" || {
  echo "进入 deploy 目录失败！请检查压缩包内目录结构是否为 deploy/Dockerfile。"
  exit 1
}

# 4. 构建 Docker 镜像
echo "开始构建 Docker 镜像 ${DOCKER_IMAGE_NAME}..."
docker build -t "$DOCKER_IMAGE_NAME" . || {
  echo "Docker 镜像构建失败！请检查 Dockerfile 语法或 Docker 服务是否正常。"
  exit 1
}
echo "镜像构建完成！"

# 5. 启动 Docker 容器（先停止并删除旧容器）
echo "开始启动 Docker 容器 ${DOCKER_CONTAINER_NAME}..."
docker stop "$DOCKER_CONTAINER_NAME" > /dev/null 2>&1
docker rm "$DOCKER_CONTAINER_NAME" > /dev/null 2>&1
docker run -d -p 8080:8080 -v /data:/app/data --name "$DOCKER_CONTAINER_NAME" "$DOCKER_IMAGE_NAME" || {
  echo "Docker 容器启动失败！请检查容器端口、资源等配置。"
  exit 1
}
echo "容器启动成功！可通过以下命令查看日志："
echo "docker logs -f ${DOCKER_CONTAINER_NAME}"
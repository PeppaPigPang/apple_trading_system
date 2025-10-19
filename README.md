# 项目简介
## 项目说明
该项目是一个苹果交易系统的后台，提供交易添加、修改、删除接口。基于 springboot 3.2.0 和 JDK 17 开发。
## 接口列表
### 添加交易接口
```
# 接口地址
http://localhost:8080/api/add
```
```
# 接口请求报文样例
{
    orderId: 20251018120303223,
    orderName: "test",
    quantity: 2,
    unit: "个",
    unitPrice: 6480
}
```
### 修改交易接口
```
# 接口地址
http://localhost:8080/api/update
```
```
# 接口请求报文样例
{
    orderId: 20251018120303223,
    orderName: "test",
    quantity: 2,
    unit: "个",
    unitPrice: 6480
}
```
### 删除交易接口
```
# 接口地址
http://localhost:8080/api/delete
```
```
# 接口请求报文样例
# 为了保持接口行为统一这里采用 JSON 报文的方式传参，暂不使用 /api/delete/${id} 的方式
{
    orderId: 20251018120303223,
}
```
### 查询交易接口
```
# 接口地址
http://localhost:8080/api/orders
```
```
# 接口请求报文样例
{
    pageNum: 1,
    pageSize: 10,
}
```
# 打包运行方式
## 打包
### 打包命令
```
# 采用 maven 对项目进行管理
mvn clean install -DskipTests
```
### 打包结果介绍
apple_trading_system_deploy 是专门的打包工程，会在 target 目录下生成 apple_trading_system_deploy-1.0-SNAPSHOT.zip 压缩包。
该压缩包内，有如下目录：
```
# 配置文件目录
config
```
```
# 部署文件目录
deploy
# 又 deploy 文件夹内有如下文件
Dockerfile
start.sh
start_docker.sh
```
```
# 日志文件目录
logs
```
```
# 其他文件目录，有造数脚本
tmp
```
## 运行
### 本地运行
```shell
# 执行 deploy 目录下的 start.sh 即可
chmod +x start.sh

sh ./start.sh
```
### idea 运行
```
# 运行下面的入口类
src/main/java/per/liyl/App.java 
```
### docker 容器运行
```shell
# 执行 deploy 目录下的 start_docker.sh 即可
chmod +x start_docker.sh

sh ./start_docker.sh
```
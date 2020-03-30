# RpcDemo

## 项目结构

* rpc-app：应用层 主要负责生成代理类，服务调用服务端
* rpc-monitor：监控层 主要负责收集服务调用数据以及链路查找
* rpc-register：注册中心，提供注册中心api，可扩展实现自己的注册中心功能
* rpc-net：网络层，负责封装底层网络调用
* rpc-common：通用依赖管理
* rpc-spring：Rpc结合spring框架
* rpc-spring-demo：Rpc结合spring框架，demo
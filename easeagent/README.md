# easeagent

## 设计

api 被打包进 boot，Bootstrap class loader 可访问。
core 被打包进 lib，EaseAgentClassLoader 可访问。

api 是会被应用到业务中的，因此业务需要可见。
将实现放到 EaseAgentClassLoader 中，避免将 Agent 内部业务暴露给业务应用。
Agent 相关的实现类将在 Bootstrap 类中被初始化，然后将实例暴露给 Agent，确保目标类中的 Interceptor 可以间接操作到隔离的类。

用于查看增强后的 class
```
-Dnet.bytebuddy.dump=/tmp/easeagent-dump
```
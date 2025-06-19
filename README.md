# ArchLibman

### 介绍

这是一个支持滚动更新的minecraft pvp客户端，更准确的说它是一个客户端构建管理器而不是传统意义上的客户端，它充分利用了mcp918的python脚本库，尽管开发还处于初期阶段，但它已经能够完成在本地的克隆仓库、编译、构建、回滚、启动游戏、打包等功能。

### 安装

##### 先决条件

* [x] 确保您安装了python2.7和python3（两个版本都需要安装）
* [x] 确保您安装了git和pip

```
git clone https://github.com/Archjh/ArchLibman.git
```

或者：

```
git init
git remote add origin https://github.com/Archjh/ArchLibman.git
git fetch --all
git checkout main  # 或你的目标分支
```
```
cd /你的项目的绝对路径
python3 ArchLibman.py
```

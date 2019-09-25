# flume配置文件模板
监控netcat相关命令telnet,将日志写入控制台
## demo.properties
### 组件类型
source	netcat
channel  memory
sink	logger
## exec-mem-hdfs.properties
收集网站用户点击流数据到hdfs
### 组件类型
source	exec	由用户访问网站在web服务器产生的日志数据，使用exec监控日志文件
channel  memory	使用内存缓冲
sink		hdfs	数据导入到hdfs
## spool-file-hdfs.properties 
收集指定目录下的日志文件到HDFS
### 组件类型
source：spooling
channel：file
sink：hdfs
## exec-mem-hive.properties
监控指定文本文件，使用flume收集到hive表里
### 组件类型
source：exec
channel：memory
sink：hive

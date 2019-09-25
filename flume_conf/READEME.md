# flume配置文件模板
##demo.properties
###组件类型
source	netcat
channel  memory
sink	logger
##exec-mem-hdfs.properties
###组件类型
source	exec	由用户访问网站在web服务器产生的日志数据，使用exec监控日志文件
channel  memory	使用内存缓冲
sink		hdfs	数据导入到hdfs
##spool-file-hdfs.properties 
###组件类型
source：spooling
channel：file
sink：hdfs
##exec-mem-hive.properties
###组件类型
source：exec
channel：memory
sink：hive

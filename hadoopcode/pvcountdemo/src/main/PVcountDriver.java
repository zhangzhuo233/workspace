package main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class PVcountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 参数中设置输入&输出路径
        args = new String[] {
                "hdfs://hadoopserver01:8020/wordcount/input",
                "hdfs://hadoopserver01:8020/wordcount/output15"
        };
        // 创建默认配置文件类的对象
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://hadoopserver01:8020");
        // 获取一个job的实例对象
        Job job = Job.getInstance(conf);
        job.setJarByClass(PVcountDriver.class);
        // 设置输入输出路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 指定使用自己的Mapper和Reducer类
        job.setMapperClass(PVcountMapper.class);
        job.setReducerClass(PVcountReducer.class);
        // 指定map输出的<key,value>类型
        job.setMapOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        // 指定reduce输出的<key,value>类型
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        // 提交到yarn上运行
        // verbose - print the progress to the user
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}

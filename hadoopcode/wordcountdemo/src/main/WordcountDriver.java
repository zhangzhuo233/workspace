package main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordcountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 设置输入输出路径
        args = new String[] {
                "hdfs://hadoopserver01:8020/wordcount/input",
                "hdfs://hadoopserver01:8020/wordcount/output9"
        };
        // 创建默认配置文件类的对象
        Configuration conf = new Configuration();
        // 获取一个job实例
        Job job = Job.getInstance(conf);
        // 设置输入输出路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 指定使用自己的Mapper和Reducer类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);
        // 指定map输出的<key,value>的类型
        job.setMapOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 指定reduce输出的<key,value>的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 提交到YARN上运行
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}

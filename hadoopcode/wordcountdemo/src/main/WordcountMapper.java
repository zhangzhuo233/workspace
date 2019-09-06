package main;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordcountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Text keyout = new Text();
    private IntWritable valueout = new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Text->String,便于后面字符串操作
        String s = value.toString();
        // 将字符串按照空格分割,存储在数组中
        String[] words = s.split(" ");
        // 将每一个word均直接输出，不用统计word的出现次数，因为reduce会统一处理
        for (String word:
             words) {
            keyout.set(word);
            context.write(keyout, valueout);
        }
    }
}

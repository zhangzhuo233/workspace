package com.bd.secondarysort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 数据 a,3 a,4...
 * keyin: 字节偏移量
 * valuein: 行
 * keyout: 自定义类型 a3
 * valueout: 3
 */
public class SecondarysortMapper extends Mapper<LongWritable, Text, CustomWritable, IntWritable> {
    private CustomWritable keyout = new CustomWritable();
    private IntWritable valueout = new IntWritable();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] fileds = str.split(",");

        // get originkey & originValue
        String originKey = fileds[0];
        int originValue = Integer.valueOf(fileds[1]);
        keyout.set(originKey, originValue);
        valueout.set(originValue);
        context.write(keyout, valueout);
    }
}

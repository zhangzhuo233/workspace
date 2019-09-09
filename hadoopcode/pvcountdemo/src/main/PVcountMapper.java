package main;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PVcountMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
    private IntWritable keyout = new IntWritable();
    private IntWritable valueout = new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Text->String
        String s = value.toString();
        // "\t"分割
        String[] filelds = s.split("\t");
        // 字段丢失6个以上的数据丢弃,精度由业务需求确定
        if (filelds.length < 30) {
            // System.out.println("字段丢失超过6条，此数据废弃");
            context.getCounter("WEB-PV Counter", "fileds length less than 30").increment(1);
            return;
        }

        Integer provinceTest = Integer.MIN_VALUE;
        String provinceId = filelds[23];
        // provinceId非数字类型的丢弃
        try {
            provinceTest = Integer.valueOf(provinceId);
        } catch (NumberFormatException e) {
            // System.out.println("省份Id异常");
            context.getCounter("WEB-PV Counter", "PROVINCE_ID NOT NUMBER").increment(1);
            return;
        }
        keyout.set(provinceTest);
        // 写入maptask
        context.write(keyout, valueout);
    }
}

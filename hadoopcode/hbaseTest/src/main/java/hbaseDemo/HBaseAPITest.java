package hbaseDemo;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseAPITest {

    public static void main(String[] args) throws IOException {
        HBaseUtils instance = HBaseUtils.getInstance("school:stu_info");

        /**
         * put one data
         */
        // instance.putOneData(Bytes.toBytes("1009"),
        //         Bytes.toBytes("info"),
        //         Bytes.toBytes("name"),
        //         Bytes.toBytes("xiaohua"));

        /**
         * put data by buffer
         */
        // List<Put> puts = new ArrayList<Put>();
        // Put put1 = new Put(Bytes.toBytes("1012"));
        // put1.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("wangwu"));
        // put1.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes("18"));
        // puts.add(put1);
        // Put put2 = new Put(Bytes.toBytes("1013"));
        // put2.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("yangzi"));
        // put2.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes("19"));
        // puts.add(put2);
        // instance.putDataByBuffer(puts);

        /**
         * put batch operations
         * 批处理
         */
        // instance.batchPut(puts);

        /**
         * 原子性操作
         *  检查写操作，如果返回false则不能put
         *  参数value如果填null，就是判断该行是否存在
         */
        // instance.checkAndPutService(Bytes.toBytes("1014"),
        //         Bytes.toBytes("info"),
        //         Bytes.toBytes("name"),
        //         Bytes.toBytes("xiaobai"),
        //         null
        // );
        /**
         * Get
         */
        // instance.getData(Bytes.toBytes("1001"),
        //         Bytes.toBytes("info"),
        //         Bytes.toBytes("name"));
        // instance.getData(Bytes.toBytes("1001"),
        //         Bytes.toBytes("info"));
        /**
         * batch get
         */
        // List<Get> getList = new ArrayList<Get>();
        // Get getObj = new Get(Bytes.toBytes("1001"));
        // getObj.addFamily(Bytes.toBytes("info"));
        // getList.add(getObj);
        //
        // Get getObj1 = new Get(Bytes.toBytes("1001"));
        // getObj1.addFamily(Bytes.toBytes("contact"));
        // getList.add(getObj1);
        //
        // instance.batchGet(getList);

        /**
         * batch delete
         */
        // List<Delete> deleteList = new ArrayList<Delete>();
        // Delete deleteObj = new Delete(Bytes.toBytes("1001"));
        // deleteObj.addColumn(Bytes.toBytes("info"),
        //         Bytes.toBytes("gender"));
        //
        // Delete deleteObj1 = new Delete(Bytes.toBytes("1001"));
        // deleteObj1.addFamily(Bytes.toBytes("family"));
        // deleteList.add(deleteObj1);
        //
        // deleteList.add(deleteObj);
        // deleteList.add(deleteObj1);
        // instance.batchDelete(deleteList);
        /**
         * scan
         */
        instance.scanService();
        /**
         * batch row
         */
        // List<Row> rows = new ArrayList<Row>();
        // Put putObj = new Put(Bytes.toBytes("1010"));
        // putObj.addColumn(Bytes.toBytes("contact"),
        //         Bytes.toBytes("address"),
        //         Bytes.toBytes("shanghai"));
        // rows.add(putObj);
        // Delete deleteObj = new Delete(Bytes.toBytes("1015"));
        // deleteObj.addFamily(Bytes.toBytes("contact"));
        // rows.add(deleteObj);
        // instance.commonBatch(rows);
    }
}
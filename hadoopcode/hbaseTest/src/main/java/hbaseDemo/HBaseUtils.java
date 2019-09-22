package hbaseDemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HBaseUtils {
    private static Connection connection;
    private Table table;
    private static HBaseUtils instance;

    private HBaseUtils(Table table) {
        this.table = table;
    }

    private static Table getTable(String tableName) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hiveserver01");
        connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        return table;
    }

    public static HBaseUtils getInstance(String tableName) throws IOException {
        // 减少进入同步的次数
        if (instance == null) {
            synchronized (HBaseUtils.class) {
                // 同步条件
                if (instance == null)
                    instance = new HBaseUtils(getTable(tableName));
            }
        }
        return instance;
    }

    /**
     * Test put one data
     */
    public void putOneData(byte[] rowKey, byte[] family, byte[] qualifier, byte[] value) throws IOException {
        Put putObj = new Put(rowKey);
        putObj.addColumn(family, qualifier, value);
        // RPC操作
        table.put(putObj);
    }

    /**
     * Test put datas by buffer
     *         优化机制(批处理不可能一个put一个RPC连接)
     *         激活客户端写缓存
     *         开启方法:
     *         旧版API：table.setAutoFlush(false)
     *              table.setWriteBufferSize();
     *              通过table.isAutoFlush()方法查看状态
     *         新版API：
     *         设置客户端写缓存的空间大小
     */
    public void putDataByBuffer(List<Put> puts) throws IOException {
        BufferedMutatorParams mutatorParams = new BufferedMutatorParams(TableName.valueOf("school:stu_info"));
        mutatorParams.writeBufferSize(2048);
        // 获得客户端写缓存的大小
        BufferedMutator bufferedMutator = connection.getBufferedMutator(mutatorParams);
        bufferedMutator.getWriteBufferSize();
        // 将put操作写入客户端写缓存
        bufferedMutator.mutate(puts);
        // 强制刷写客户端写缓存，将数据发送给hbase server
        bufferedMutator.flush();
    }

    /**
     * put batch operations
     * 批处理
     */
    public void batchPut(List<Put> puts) throws IOException {
        table.put(puts);
    }

    /**
     * 原子性操作
     *  检查写操作，如果返回false则不能put
     */
    public void checkAndPutService(byte[] rowKey, byte[] family, byte[] qualifier, byte[] putValue, byte[] compValue) throws IOException {
        Put putObj = new Put(rowKey);
        putObj.addColumn(family, qualifier, putValue);
        boolean res = table.checkAndPut(rowKey, family, qualifier, compValue, putObj);
        System.out.println("checkAndPut result: " + res);
    }

    /**
     * utils->showResult
     */
    public void showResult(Result result) {
        // Return the array of Cells backing this Result instance
        // result.rawCells();
        for (Cell cell:
             result.rawCells()) {
            String row = Bytes.toString(CellUtil.cloneRow(cell));
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            long version = cell.getTimestamp();

            System.out.println(row + " " + family + ":" + qualifier + " " + value + " " + version);
        }
    }

    /**
     * Get by family:qualifier
     */
    public void getData(byte[] rowKey, byte[] family, byte[] qualifier) throws IOException {
        Get getObj = new Get(rowKey);
        getObj.addColumn(family, qualifier);
        Result result = table.get(getObj);
        // System.out.println(result);
        showResult(result);
    }

    /**
     * Get by family
     */
    public void getData(byte[] rowKey, byte[] family) throws IOException {
        Get getObj = new Get(rowKey);
        getObj.addFamily(family);
        Result result = table.get(getObj);
        // System.out.println(result);
        showResult(result);
    }

    /**
     * batch get
     */
    public void batchGet(List<Get> gets) throws IOException {
        Result[] results = table.get(gets);
        for (Result res:
             results) {
            showResult(res);
        }
    }

    /**
     * batch delete
     */
    public void batchDelete(List<Delete> deleteList) throws IOException {
        table.delete(deleteList);
    }

    /**
     * scan
     */
    public void scanService() throws IOException {
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        for (Result res:
             scanner) {
            showResult(res);
        }
        table.close();
    }
    /**
     * common batch
     */
    public void commonBatch(List<Row> list) {
        Object[] results = new Object[list.size()];
        try {
            table.batch(list, results);
            for (Object obj:
                 results) {
                System.out.println(results);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

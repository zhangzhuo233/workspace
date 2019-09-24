package phoenixDemo;

import org.junit.Test;

import java.sql.*;

/**
 * @program: hbaseTest
 * @description: phoenix operation
 * @author: Mr.zhang
 * @create: 2019-09-24 11:56
 **/

public class PhoenixAPITest {
    /**
     * 创建phoenix的connection对象
     * @return
     */
    public static Connection getConnection() {
        try {
            // load driver
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");

            // jdbc的url类似为：jdbc:phoenix [:<zookeeper quorum> [:<port number> ] [ :<root node> ]]
            // 需要引用三个参数：hbase.zookeeper.quorum，hbase.zookeeper.property.clientPort，zookeeper.znode.parent
            // 这些参数可以缺省不填而在hbase-site.xml中定义
            return DriverManager.getConnection("jdbc:phoenix");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 建表
     */
    @Test
    public void create() {

        Connection conn = null;

        try {
            // get connection
            conn = PhoenixAPITest.getConnection();

            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // check if the table exist
            ResultSet rs = conn.getMetaData().getTables(null, null, "USER01", null);
            if (rs.next()) {
                System.out.println("table user01 is exist...");
                return;
            }

            // create sql
            String sql = "CREATE TABLE user01 (id varchar PRIMARY KEY,INFO.name varchar ,INFO.passwd varchar)";

            PreparedStatement ps = conn.prepareStatement(sql);

            // execute
            ps.execute();
            System.out.println("create success...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 插入/更新数据
     */
    @Test
    public void upsert() {

        Connection conn = null;
        try {
            // get connection
            conn = PhoenixAPITest.getConnection();

            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            String sql = "upsert into user01(id, INFO.name, INFO.passwd) values('001', 'admin', 'admin')";

            PreparedStatement ps = conn.prepareStatement(sql);

            // execute upsert
            String msg = ps.executeUpdate() > 0 ? "insert success..."
                    : "insert fail...";

            // you must commit
            conn.commit();
            System.out.println(msg);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 查询
     */
    @Test
    public void query() {

        Connection conn = null;
        try {
            // get connection
            conn = PhoenixAPITest.getConnection();

            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            String sql = "select * from user01";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("id" + "\t" + "name" + "\t" + "passwd");
            System.out.println("======================");

            if (rs != null) {
                while (rs.next()) {
                    System.out.print(rs.getString("id") + "\t");
                    System.out.print(rs.getString("name") + "\t");
                    System.out.println(rs.getString("passwd"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 删除
     */
    @Test
    public void delete() {

        Connection conn = null;
        try {
            // get connection
            conn = PhoenixAPITest.getConnection();

            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            String sql = "delete from user01 where id='001'";

            PreparedStatement ps = conn.prepareStatement(sql);

            // execute upsert
            String msg = ps.executeUpdate() > 0 ? "delete success..."
                    : "delete fail...";

            // you must commit
            conn.commit();
            System.out.println(msg);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 删除表
     */
    @Test
    public void drop() {

        Connection conn = null;
        try {
            // get connection
            conn = PhoenixAPITest.getConnection();

            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            String sql = "drop table user01";


            PreparedStatement ps = conn.prepareStatement(sql);

            // execute
            ps.execute();

            System.out.println("drop success...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

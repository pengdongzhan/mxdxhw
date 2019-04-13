package thrift.generated.Utils;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import thrift.generated.model.Person;


public class JdbcUtils {
    //数据库地址
    //数据库用户名
    //数据库密码
    public static final String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "test";
    //驱动信息
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet resultSet;
    public JdbcUtils() {
        // TODO Auto-generated constructor stub
        try{
            Class.forName(DRIVER);
            System.out.println("数据库连接成功！");

        }catch(Exception e){

        }
    }

    /**
     * 获得数据库的连接
     * @return
     */
    public Connection getConnection(){
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }


    /**
     * 增加、删除、改
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public boolean updateByPreparedStatement(String sql, List<Object>params)throws SQLException{
        boolean flag = false;
        int result = -1;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            result = pstmt.executeUpdate();
            flag = result > 0 ? true : false;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return flag;
    }

    /**
     * 查询单条记录
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<String, Object> findSimpleResult(String sql, List<Object> params) throws SQLException{
        Map<String, Object> map = new HashMap<String, Object>();
        int index  = 1;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            if(params != null && !params.isEmpty()){
                for(int i=0; i<params.size(); i++){
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();//返回查询结果
            ResultSetMetaData metaData = resultSet.getMetaData();
            int col_len = metaData.getColumnCount();
            while(resultSet.next()){
                for(int i=0; i<col_len; i++ ){
                    String cols_name = metaData.getColumnName(i+1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if(cols_value == null){
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return map;
    }

    /**查询多条记录
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> findModeResult(String sql, List<Object> params) throws SQLException{
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeConnection();
        }

        return list;
    }

    /**通过反射机制查询单条记录
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> T findSimpleRefResult(String sql, List<Object> params,
                                     Class<T> cls )throws Exception{
        T resultObject = null;
        int index = 1;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            if(params != null && !params.isEmpty()){
                for(int i = 0; i<params.size(); i++){
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData  = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while(resultSet.next()){
                //通过反射机制创建一个实例
                resultObject = cls.newInstance();
                for(int i = 0; i<cols_len; i++){
                    String cols_name = metaData.getColumnName(i+1);
                    Object cols_value = resultSet.getObject(cols_name);
//                    if(cols_value == null){
//                        cols_value = "";
//                    }
                    Field field = cls.getDeclaredField(cols_name);
                    field.setAccessible(true); //打开javabean的访问权限
                    field.set(resultObject, cols_value);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return resultObject;

    }

    /**通过反射机制查询多条记录
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> List<T> findMoreRefResult(String sql, List<Object> params,
                                         Class<T> cls )throws Exception {
        //首先定义一个list用于返回
        List<T> list = new ArrayList<T>();
        int index = 1;
        try{
            //创建连接并预编译
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            //循环获取参数，放置到？里
            if(params != null && !params.isEmpty()){
                for(int i = 0; i<params.size(); i++){
                    pstmt.setObject(index++, params.get(i));
                }
            }

            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData  = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while(resultSet.next()){
                //通过反射机制创建一个实例
                T resultObject = cls.newInstance();
                for(int i = 0; i<cols_len; i++){
                    //获取当前记录的列名和值
                    String cols_name = metaData.getColumnName(i+1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if(cols_value == null){
                        cols_value = "";
                    }
                    //根据列名定位到对象中该域
                    Field field = cls.getDeclaredField(cols_name);
                    //设置该域可访问
                    field.setAccessible(true); //打开javabean的访问权限
                    //将对象中该域的值设置为列值
                    field.set(resultObject, cols_value);
                }
                //处理好一条记录，放入list
                list.add(resultObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return list;
    }

    /**
     * 释放数据库连接
     */
    public void closeConnection(){
        try {
            if(resultSet != null){
                resultSet.close();
            }
            if (pstmt != null){
                pstmt.close();
            }
            if(connection != null){
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

//    /**
//     * @param args
//     */
//    public static void main(String[] args) throws SQLException {
//        // TODO Auto-generated method stub
//        JdbcUtils jdbcUtils = new JdbcUtils();
//        jdbcUtils.getConnection();
//
//        /*******************增*********************/
//		/*		String sql = "insert into user (username, pswd) values (?, ?), (?, ?), (?, ?)";
//		List<Object> params = new ArrayList<Object>();
//		params.add("xiaomin");
//		params.add("123xiaoming");
//		params.add("zhangsan");
//		params.add("zhangsan");
//		params.add("lisi");
//		params.add("lisi000");
//		try {
//			boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);
//			System.out.println(flag);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}*/
//
//
//        /*******************删*********************/
//        //删除名字为张三的记录
//		/*		String sql = "delete from userinfo where username = ?";
//		List<Object> params = new ArrayList<Object>();
//		params.add("小明");
//		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);*/
//
//        /*******************改*********************/
//        //将名字为李四的密码改了
//		/*		String sql = "update userinfo set pswd = ? where username = ? ";
//		List<Object> params = new ArrayList<Object>();
//		params.add("lisi88888");
//		params.add("李四");
//		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);
//		System.out.println(flag);*/
//
//        /*******************查*********************/
//        //不利用反射查询多个记录
//		/*		String sql2 = "select * from userinfo ";
//		List<Map<String, Object>> list = jdbcUtils.findModeResult(sql2, null);
//		System.out.println(list);*/
//
//        //利用反射查询 单条记录
//        String sql = "select * from userinfo where username = ? ";
//        List<Object> params = new ArrayList<Object>();
//        params.add("李四");
//        Person person;
//        try {
//            person = jdbcUtils.findSimpleRefResult(sql, params, Person.class);
//            System.out.print(person);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}

package thrift.generated.Utils;

public class Constants {
    public static final String YES = "0";
    public static final String NO = "1";
    public static final String SUCCESS = "0";
    public static final String FAIL = "1";
    public static final String MAX_ID_SQL = "select max(id) as id from user where 1=1";
    public static final String SELECT_USER_BYNAME_SQL = "select * from user where userName = ?";
    public static final String ADDUSER_SQL = "insert into USER(id,userName,password,phone,email) values (?,?,?,?,?)";
    public static final String SELECT_USER_BYNAME_PASS_SQL = "select * from user where userName = ? and passWord=?";
    public static final String DELETE_USER_BYNAME_SQL = "delete from user where userName = ?";
    public static final String SELECT_ALLUSER_SQL = "select * from user where 1=1";
}

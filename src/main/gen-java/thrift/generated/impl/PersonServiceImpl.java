package thrift.generated;
import org.apache.thrift.TException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import thrift.generated.DataException;
import thrift.generated.Person;
import thrift.generated.PersonService;

public class PersonServiceImpl implements PersonService.Iface{


    Connection conn = null;
    ResultSet rs = null;
    java.sql.Statement st=null;
    java.sql.Statement st1=null;
    java.sql.Statement st2=null;
    java.sql.Statement st3=null;
    java.sql.Statement st4=null;
    java.sql.Statement st5=null;
    java.sql.Statement st6=null;
    java.sql.Statement st7=null;
    java.sql.Statement st8=null;
    java.sql.Statement st9=null;
    ResultSet a,b,c,d,e,f,g,h,i,j;
    public PersonServiceImpl() throws RemoteException{

        try {
            System.out.println("成功加载MySQL驱动程序");

            //创建数据库连接
            String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
            String dbuser="root";
            String dbpassword="test";

            //创建执行对象
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,dbuser,dbpassword);

            System.out.println("成功连接");

            st = conn.createStatement();
            st1 = conn.createStatement();
            st2 = conn.createStatement();
            st3 = conn.createStatement();
            st4 = conn.createStatement();
            st5 = conn.createStatement();
            st6 = conn.createStatement();
            st7 = conn.createStatement();
            st8 = conn.createStatement();
            st9 = conn.createStatement();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Person getPersonByUsername(String username) throws DataException, TException {
        System.out.println("Got client Param:" + username);

        Person person = new Person();
        person.setUsername(username);
        person.setAge(32);
        person.setMarried(true);

        return person;
    }

    @Override
    public int deletePerson(String username) throws DataException, TException {
        return 0;
    }

    @Override
    public Person getAllPerson() throws DataException, TException {
        return null;
    }

    public int register(String sql){
        int num=0;
        try{
            num=st.executeUpdate(sql);
        }
        catch(SQLException e){}
        return num;
    }

    @Override
    public Person savePerson(Person person) throws DataException, TException,SQLException{
        System.out.println("Got Client Param: ");
        Person per = new Person();
        ResResult resResult = new ResResult();
        try {

                int num = 0;
                int maxid=0;
                a = st1.executeQuery("select max(id) as id from user where 1=1");
                if (a.next()) {
                    maxid = a.getInt("id");
                    maxid = maxid + 1;
                }
                 h = st.executeQuery("select * from user where userName='" + person.getUsername() + "'");

                if (h.next()) {
                    resResult.setMessage("客户端注册失败！其所使用的用户名已经存在！");
                    resResult.setResCode("1");
                    per.setResResult(resResult);
                    System.out.println("客户端注册失败！其所使用的用户名已经存在！");
                    //       return false;
                } else {
                    System.out.println(maxid);
                    String sql = "insert into USER(id,userName,password) values ('" +maxid+"','" + person.getUsername() + "','" + person.getAge() + "')";
                    num = st.executeUpdate(sql);
                    System.out.println("客户端注册成功！");
                    resResult.setMessage("客户端注册成功！");
                    resResult.setResCode("0");
                    per.setUsername(person.getUsername());
                    per.setResResult(resResult);
                    System.out.println(person.getUsername());
                    System.out.println(person.getAge());
                    System.out.println(person.isMarried());
                }
                return per;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            resResult.setMessage("系统异常！");
            resResult.setResCode("1");
            per.setResResult(resResult);
            e.printStackTrace();
        }
            return per;
    }
}
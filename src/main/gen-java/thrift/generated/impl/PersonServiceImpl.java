package thrift.generated.impl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import thrift.generated.DataException;
import thrift.generated.Utils.Constants;
import thrift.generated.Utils.JdbcUtils;
import thrift.generated.model.Person;
import thrift.generated.model.ResResult;
import thrift.generated.service.PersonService;

public class PersonServiceImpl implements PersonService.Iface{

    public static Log log = LogFactory.getLog(PersonServiceImpl.class);
    public PersonServiceImpl(){
    }

    @Override
    public Person getPersonByUsername(String username) throws DataException, TException {
        System.out.println("Got client Param:" + username);
        log.info("Got client Param:" + username);
        Person person = new Person();
        person.setUsername(username);
        person.setAge(32);
        person.setMarried(true);

        return person;
    }

    @Override
    public int deletePerson(String username) throws DataException, TException {
        System.out.println("deletePerson--Got Client Param: ");
        log.info("deletePerson--Got Client Param: ");
        ResResult resResult = new ResResult();
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
        try {
            List<Object> parmas = new ArrayList<Object>();
            parmas.add(username);
            boolean sqlresult = jdbcUtils.updateByPreparedStatement(Constants.DELETE_USER_BYNAME_SQL,parmas);
            if(sqlresult){
                resResult.setMessage("删除用户成功！");
                resResult.setResCode(Constants.SUCCESS);
                System.out.println("删除用户成功");
                log.info("登录用户成功");
                return 1;
            } else {
                System.out.println("删除用户"+username+"失败，找不到该用户");
            }
            return 0;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            resResult.setMessage("系统异常！");
            resResult.setResCode(Constants.FAIL);
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public List<Person> getAllPerson() throws DataException, TException {
        System.out.println("Got Client Param: ");
        log.info("Got Client Param: ");
        List<Person>  list1 = new ArrayList<Person>();
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
        try {
            list1 = jdbcUtils.findMoreRefResult(Constants.SELECT_ALLUSER_SQL,null,Person.class);
            if(list1 != null && list1.size() > 0)
                log.info("客户信息查询成功！");
        } catch (Exception e) {
            log.error("查询所有人异常");
        }
        return list1;
    }

    public boolean register(String sql){
        JdbcUtils jdbcUtils = new JdbcUtils();
        boolean isAddSuc = false;
        try {
            isAddSuc = jdbcUtils.updateByPreparedStatement(Constants.ADDUSER_SQL,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAddSuc;
    }

    /**
     * 用户登录
     * @return
     * @throws DataException
     * @throws TException
     */
    @Override
    public Person userLogin(Person person) throws DataException, TException {
        System.out.println("userLogin--Got Client Param: ");
        log.info("userLogin--Got Client Param: ");
        Person per = new Person();
        ResResult resResult = new ResResult();
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
        try {

            int num = 0;
//             h = st.executeQuery("select * from user where userName='" + person.getUsername() + "'");
            List<Object> parmas = new ArrayList<Object>();
            parmas.add(person.getUsername());
            parmas.add(person.getPassword());
            Person person1 = jdbcUtils.findSimpleRefResult(Constants.SELECT_USER_BYNAME_PASS_SQL,parmas,Person.class);
            if(person1 != null){
                resResult.setMessage("登录成功！");
                resResult.setResCode(Constants.SUCCESS);
                per.setResResult(resResult);
                System.out.println("登录成功");
                log.info("登录成功");
            } else {
                    System.out.println("客户端登录失败，该用户不存在或密码错！");
                    resResult.setMessage("客户端登录失败，该用户不存在或密码错误！");
                    resResult.setResCode(Constants.FAIL);
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
            resResult.setResCode(Constants.FAIL);
            per.setResResult(resResult);
            e.printStackTrace();
        }
        return per;
    }

    /**
     * 注册用户
     * @param person
     * @return
     * @throws DataException
     * @throws TException
     * @throws SQLException
     */
    @Override
    public Person savePerson(Person person) throws DataException, TException{
        System.out.println("Got Client Param: ");
        log.info("Got Client Param: ");
        Person per = new Person();
        ResResult resResult = new ResResult();
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
        try {

            int num = 0;
            int maxId=0;
            Map<String, Object> map = jdbcUtils.findSimpleResult(Constants.MAX_ID_SQL,null);
//            a = st1.executeQuery(Constants.MAX_ID_SQL);
//            if (a.next()) {
            maxId = (int) map.get("id");
            maxId = maxId + 1;
//            }

//             h = st.executeQuery("select * from user where userName='" + person.getUsername() + "'");
            List<Object> parmas = new ArrayList<Object>();
            parmas.add(person.getUsername());
            Person person1 = jdbcUtils.findSimpleRefResult(Constants.SELECT_USER_BYNAME_SQL,parmas,Person.class);
            if(person1 != null){
                resResult.setMessage("客户端注册失败！其所使用的用户名已经存在！");
                resResult.setResCode(Constants.FAIL);
                per.setResResult(resResult);
                System.out.println("客户端注册失败！其所使用的用户名已经存在！");
               log.info("客户端注册失败！其所使用的用户名已经存在！");
            } else {
                System.out.println(maxId);
//                String sql = "insert into USER(id,userName,password) values ('" +maxId+"','" + person.getUsername() + "','" + person.getAge() + "')";
//                num = st.executeUpdate(sql);
                List<Object> params2 = new ArrayList<Object>();
                params2.add(maxId);
                params2.add(person.getUsername());
                params2.add(person.getPassword());
                params2.add(person.getPhone());
                params2.add(person.getEmail());
                boolean isAddSuc = jdbcUtils.updateByPreparedStatement(Constants.ADDUSER_SQL,params2);
                if(isAddSuc) {
                    System.out.println("客户端注册成功！");
                    resResult.setMessage("客户端注册成功！");
                    resResult.setResCode(Constants.SUCCESS);
                    per.setUsername(person.getUsername());
                    per.setResResult(resResult);
                    System.out.println(person.getUsername());
                    System.out.println(person.getAge());
                    System.out.println(person.isMarried());
                }
            }
            return per;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            resResult.setMessage("系统异常！");
            resResult.setResCode(Constants.FAIL);
            per.setResResult(resResult);
            e.printStackTrace();
        }
            return per;
    }
}
package thrift.generated;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import java.util.List;
import java.util.Scanner;

import thrift.generated.model.Person;
import thrift.generated.service.PersonService;

//服务端的协议和客户端的协议要一致
public class ThriftClient {
    public static Log log = LogFactory.getLog(ThriftClient.class);
    public static void main(String[] args) {
        PropertyConfigurator.configure("E://mxdxhw/src/log4j.properties");
        TTransport tTransport = new TFastFramedTransport(new TSocket("localhost",8899),600);
        TProtocol tProtocol = new TCompactProtocol(tTransport);
        PersonService.Client client = new PersonService.Client(tProtocol);


        try{

            tTransport.open();

            // Create a Parser
            CommandLineParser parser = new BasicParser( );
            Options options = new Options( );
            options.addOption("a", "adduser", false, "adduser");
            options.addOption("o", "outuser", false, "outuser" );
            options.addOption("i", "inuser", false, "inuser");
            options.addOption("d", "deleteuser", false, "deleteuser");
            options.addOption("l", "listuser", false, "listuser");
            options.addOption("n", "name", true, "name");
            options.addOption("p", "password", true, "password");
            options.addOption("e", "email", true, "eamil");
            options.addOption("m", "moblie", true, "moblie");
            // Parse the program arguments
            CommandLine commandLine = parser.parse( options, args );
            // Set the appropriate variables based on supplied options
            boolean verbose = false;
            //  获取相关变量
            String name = commandLine.getOptionValue('n');
            String password =commandLine.getOptionValue('p');
            String email = commandLine.getOptionValue('e');
            String phone = commandLine.getOptionValue('m');


            if( commandLine.hasOption('a') ) {
                //创建一个对象用于注册
                Person person2 = new Person();
                person2.setUsername(name);
                person2.setPassword(password);
                person2.setEmail(email);
                person2.setPhone(phone);
                Person per1 = client.savePerson(person2);
                System.out.println(per1.getResResult().getMessage());

                System.out.println( "Help Message");
                //  System.exit(0);
            }
            if( commandLine.hasOption('o') ) {
                int result;
                System.out.println("您已登出，请重新进入系统登录login或注册register");
            }
            if( commandLine.hasOption('i') ) {

                //创建一个对象用于登录
                Person person2 = new Person();
                person2.setUsername(name);
                person2.setPassword(password);
                Person per = client.userLogin(person2);
                System.out.println(per.getResResult().getMessage());
            }
            if( commandLine.hasOption('d') ) {
                int result;

                //   System.out.println("请输入删除登录密码：");
                //  String password = sc.nextLine();
                //创建一个对象用于登录
                Person person2 = new Person();
                person2.setUsername(name);
                //      person2.setPassword(password);
                result = client.deletePerson(name);
                if(result==1)
                    System.out.println("删除用户成功");
                else
                    System.out.println("删除用户失败，该用户不存在");
            }
            if( commandLine.hasOption('l') ) {
                List<Person> list = client.getAllPerson();
                System.out.println("用户信息如下");
                for (Person p :list){
                    System.out.println("用户"+p.getUsername()+"邮箱： "+p.getEmail()+"电话  "+p.getPhone());
                }
            }

            else{
                System.out.println("bad request!");
                log.info("bad request!");
            }
        }catch (Exception ex){
            throw new  RuntimeException(ex.getMessage(),ex);
        }finally {
            tTransport.close();
        }
    }
}
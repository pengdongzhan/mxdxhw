package thrift.generated;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

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
            // 如果要从另一台启动了RMI注册服务的机器上查找hello实例
            // HelloInterface hello =
            // (HelloInterface)Naming.lookup("//192.168.1.105:1099/Hello");

            //控制台输入提示
            System.out.println("欢迎登录agenda系统，请注册");
            log.info("欢迎登录agenda系统，请注册");

            Scanner sc = null;
            sc = new Scanner(System.in);
            String choice = sc.nextLine();

            //如果是以注册开始
                System.out.println("请输入注册的用户名：");
                String name = sc.nextLine();

                //创建一个对象用于注册
                Person person2 = new Person();
                person2.setUsername(name);

                Person per1 = client.savePerson(person2);
                System.out.println(per1.getResResult().getMessage());

        }catch (Exception ex){
            throw new  RuntimeException(ex.getMessage(),ex);
        }finally {
            tTransport.close();
        }
    }
}
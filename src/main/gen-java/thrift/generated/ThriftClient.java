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
            System.out.println("欢迎登录agenda系统，请选择操作");
            log.info("欢迎登录agenda系统，请选择操作");
            System.out.println("1、用户注册");
            System.out.println("2、用户登录");
            System.out.println("3、用户登出");
            System.out.println("4、用户查询");
            System.out.println("5、用户删除");


            Scanner sc = null;
            sc = new Scanner(System.in);
            String choice = sc.nextLine();

            //如果是以注册开始
            if(choice.startsWith("1")) {
                System.out.println("请输入注册的用户名：");
                String name = sc.nextLine();
                System.out.println("请设置您的密码：");
                String password = sc.nextLine();
                System.out.println("请设置您的邮箱：");
                String email = sc.nextLine();
                System.out.println("请设置您的电话：");
                String phone = sc.nextLine();
                //创建一个对象用于注册
                Person person2 = new Person();
                person2.setUsername(name);
                person2.setPassword(password);
                person2.setEmail(email);
                person2.setPhone(phone);
                Person per1 = client.savePerson(person2);
                System.out.println(per1.getResResult().getMessage());
            }else if(choice.startsWith("2")) {
                System.out.println("请输入登录的用户名：");
                String name = sc.nextLine();
                System.out.println("请输入登录密码：");
                String password = sc.nextLine();
                //创建一个对象用于登录
                Person person2 = new Person();
                person2.setUsername(name);
                person2.setPassword(password);
                Person per = client.userLogin(person2);
                System.out.println(per.getResResult().getMessage());

            }else if(choice.startsWith("5")) {
                int result;
                System.out.println("请输入需要删除的用户名：");
                String name = sc.nextLine();
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
            else if(choice.startsWith("3")) {
                int result;
                System.out.println("您已登出，请重新进入系统登录login或注册register");
            }
            else if(choice.startsWith("4")) {
               List<Person> list = client.getAllPerson();
               System.out.println("用户信息如下");
               for (Person p :list){
                   System.out.println("用户"+p.getUsername()+"邮箱： "+p.getEmail()+"电话  "+p.getPhone());
               }

            }
            /*     if(client.savePerson(person2))
                {
                    System.out.println("注册成功！");
                }else {
                    System.out.println("注册失败！");
                }
            }
            //添加会议
                /*
                else if(request.startsWith("add")){
                    System.out.println("请输入用户名，密码，会议开始时间，会议结束时间，会议标题，会议参与者姓名（用逗号隔开）");
                    String r = keyboard.readLine();
                    String[] s = r.split(",", 6);
                    String name = s[0];
                    String password = s[1];
                    String start = s[2];
                    String end = s[3];
                    String title = s[4];
                    String otherusername = s[5];
                    int i = client.addMeeting(name, password, start, end, title, otherusername);
                    switch (i) {
                        case 0:
                            System.out.println("用户名或密码错误！");
                            break;
                        case -1:
                            System.out.println("用户名未注册！");
                            break;
                        case -2:
                            System.out.println("会议参与者未注册！");
                            break;
                        case -3:
                            System.out.println("用户时间冲突！");
                            break;
                        case -4:
                            System.out.println("会议参与者时间冲突！");
                            break;
                        case 1:
                            System.out.println("添加会议成功！");
                            break;
                        default:
                            break;
                    }
                    //查询会议
                }else if(request.startsWith("query")){
                    System.out.println("请输入用户名，密码，开始时间，结束时间（用逗号隔开）");
                    String r = keyboard.readLine();
                    String[] s = r.split(",", 4);
                    String name = s[0];
                    String password = s[1];
                    String start = s[2];
                    String end = s[3];
                    String[][] list = client.search(name, password, start, end);
                    if(list != null) {
                        for (String[] i : list) {
                            System.out.print("ID: "+ i[0] + " ");
                            System.out.print("开始时间: "+ i[1] + " ");
                            System.out.print("结束时间: "+ i[2] + " ");
                            System.out.print("标题: "+ i[3] + " ");
                            System.out.print("参与者: "+ i[4] + " ");
                            System.out.println();
                        }
                    }else {
                        System.out.println("查询失败！");
                    }
                    //删除会议
                }else if(request.startsWith("delete")){
                    System.out.println("请输入用户名，密码，id（用逗号隔开）");
                    String r = keyboard.readLine();
                    String[] s = r.split(",", 3);
                    String name = s[0];
                    String password = s[1];
                    int id = Integer.parseInt(s[2]);
                    if(client.delete(name, password, id)) {
                        System.out.println("删除成功！");
                    }else {
                        System.out.println("删除失败！");
                    }
                    //清除会议
                }else if(request.startsWith("clear")){
                    System.out.println("请输入用户名，密码（用逗号隔开）");
                    String r = keyboard.readLine();
                    String[] s = r.split(",", 2);
                    String name = s[0];
                    String password = s[1];
                    if(client.clear(name, password)) {
                        System.out.println("清除成功！");
                    }else {
                        System.out.println("清除失败！");
                    }
                }
                */
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
package thrift.generated;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import java.util.Scanner;

import thrift.generated.Person;
import thrift.generated.PersonService;

//服务端的协议和客户端的协议要一致
public class ThriftClient {
    public static void main(String[] args) {

        TTransport tTransport = new TFastFramedTransport(new TSocket("localhost",8899),600);
        TProtocol tProtocol = new TCompactProtocol(tTransport);
        PersonService.Client client = new PersonService.Client(tProtocol);

        try{

            tTransport.open();
            // 如果要从另一台启动了RMI注册服务的机器上查找hello实例
            // HelloInterface hello =
            // (HelloInterface)Naming.lookup("//192.168.1.105:1099/Hello");

            //控制台输入提示
            System.out.println("请选择操作");
            System.out.println("register");
            System.out.println("add");
            System.out.println("query");
            System.out.println("delete");
            System.out.println("clear");
            System.out.println("login");

            Scanner sc = null;
            sc = new Scanner(System.in);
            String choice = sc.nextLine();

            //如果是以注册开始
            if(choice.startsWith("regist")) {
                System.out.println("请输入注册的用户名：");
                String name = sc.nextLine();
                System.out.println("请设置您的密码：");
                int password = sc.nextInt();
                //创建一个对象用于注册
                Person person2 = new Person();
                person2.setUsername(name);
                person2.setAge(password);
                person2.setMarried(true);

                client.savePerson(person2);
            }else if(choice.startsWith("login")) {
                System.out.println("请输入登录的用户名：");
                String name = sc.nextLine();
                System.out.println("请输入登录密码：");
                int password = sc.nextInt();
                //创建一个对象用于注册
                Person person2 = new Person();
                person2.setUsername(name);
                person2.setAge(password);
                person2.setMarried(false);

                client.savePerson(person2);
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
                }
        }catch (Exception ex){
            throw new  RuntimeException(ex.getMessage(),ex);
        }finally {
            tTransport.close();
        }
    }
}
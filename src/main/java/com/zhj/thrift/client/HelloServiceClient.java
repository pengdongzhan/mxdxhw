package com.zhj.thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.zhj.thrift.server.HelloService;

public class HelloServiceClient {

    public static void main(String[] args) {

        try {
            // 设置调用的服务地址-端口
            TTransport transport = new TSocket("127.0.0.1", 9090);
            //  使用二进制协议
            TProtocol protocol = new TBinaryProtocol(transport);
            // 使用的接口
            HelloService.Client client = new HelloService.Client(protocol);
            //打开socket
            transport.open();
            System.out.println("求和："+client.add(100, 150));
            System.out.println(client.getLong(System.currentTimeMillis()));
            System.out.println(client.sayHello("李四"));
            System.out.println(client.getShort((short)10));

        } catch (TTransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
package com.zhj.thrift.server.impl;

        import org.apache.thrift.TException;
        import com.zhj.thrift.server.HelloService;

public class HelloServiceImpl implements HelloService.Iface {

    @Override
    public int add(int num1, int num2) throws TException {
        return num1+num2;
    }

    @Override
    public short getShort(short value) throws TException {
        return (short) (value*2);
    }

    @Override
    public long getLong(long value) throws TException {
        Long l_value = value - 24*60*60*1000;
        return l_value;
    }

    @Override
    public String sayHello(String name) throws TException {
        return "hello " + name;
    }

}

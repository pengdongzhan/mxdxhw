package thrift.generated;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import thrift.generated.impl.PersonServiceImpl;
import thrift.generated.service.PersonService;

public class ThriftServer {
    public static Log log = LogFactory.getLog(ThriftClient.class);
    public static void main(String[] args) throws Exception{

        //加载log4j配置文件
        PropertyConfigurator.configure("E://mxdxhw/src/log4j.properties");
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        //范型就是实现的接收类
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        //表示协议层次（压缩协议）
        arg.protocolFactory(new TCompactProtocol.Factory());
        //表示传输层次
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        //半同步半异步的server
        TServer server = new THsHaServer(arg);

        System.out.println("Thrift Server started!");
        log.info("Thrift Server started!");

        //死循环，永远不会退出
        server.serve();
    }
}
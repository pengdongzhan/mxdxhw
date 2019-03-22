package thrift.generated;
import org.apache.thrift.TException;
import thrift.generated.DataException;
import thrift.generated.Person;
import thrift.generated.PersonService;

public class PersonServiceImpl implements PersonService.Iface{

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
    public void savePerson(Person person) throws DataException, TException {
        System.out.println("Got Client Param: ");
        if("李四".equals(person.getUsername())){
            System.out.println("注册失败！当前用户已存在");
        }else{
            System.out.println("注册成功");
            System.out.println(person.getUsername());
            System.out.println(person.getAge());
            System.out.println(person.isMarried());
        }

    }
}
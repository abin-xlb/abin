import abin.abin;
import abin.entity.Employee;
import abin.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(classes = abin.class)
public class test {
    @Autowired
    private EmployeeService employeeService;
    @Test
    public void add(){
        ArrayList<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Employee employee = new Employee();
            employee.setName("jnb" + i);
            employee.setUsername("yui"+i);
            employee.setPassword("123456");
            employee.setIdNumber("134567676878767655");
            employee.setSex("男");
            employee.setPhone("13443876546");
            employees.add(employee);
        }
        employeeService.saveBatch(employees);
    }

    public static void main(String[] args) {
        test test = new test();
        test.m1();
        System.out.println("结束");
    }

    public void m1(){
        System.out.println("111");
//        try {
//            m2();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        m2();
        System.out.println("11111");
    }

    private void m2() {
        System.out.println("222");
                try {
            m3();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        m3();
        System.out.println("2222222");
    }

    private void m3() {
        System.out.println("333");
        System.out.println(10/0);
        System.out.println("33333333");
    }

}

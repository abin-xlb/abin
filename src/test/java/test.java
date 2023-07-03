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

}

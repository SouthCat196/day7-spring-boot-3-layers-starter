package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.exception.EmployeeSalaryNotValidException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    protected EmployeeRepository employeeRepository;

    @InjectMocks
    protected EmployeeService employeeService;

    @Test
    void should_return_the_given_employees_when_getAllEmployees() {
        //given
        Employee employee = new Employee(1, "Lucy", 18, Gender.FEMALE, 8000.0);
        when(employeeRepository.getAll()).thenReturn(List.of(employee));

        //when
        List<Employee> allEmployees = employeeService.getAllEmployees();

        //then
        assertEquals(1, allEmployees.size());
        assertEquals("Lucy", allEmployees.get(0).getName());
    }

    @Test
    void should_return_the_created_employee_when_create_given_a_employee() {
        //given
        Employee lucy = new Employee(1, "Lucy", 18, Gender.FEMALE, 8000.0);
        when(employeeRepository.addEmployee(any())).thenReturn(lucy);

        //when
        Employee createdEmployee = employeeService.creat(lucy);

        //then
        assertEquals("Lucy", createdEmployee.getName());
    }

    @Test
    void should_throw_employee_age_not_valid_exception_when_create_employee_given_employee_with_age_is_6() {
        // Given
        Employee employee = new Employee(1, "Kitty", 6, Gender.FEMALE, 8000.0);
        // When
        // Then
        assertThrows(EmployeeAgeNotValidException.class, () -> employeeService.creat(employee));
        verify(employeeRepository, never()).addEmployee(any());
    }

    @Test
    void should_throw_employee_age_not_valid_exception_when_create_employee_given_employee_with_age_is_90() {
        // Given
        Employee employee = new Employee(1, "Tom", 90, Gender.MALE, 800000.0);
        // When
        // Then
        assertThrows(EmployeeAgeNotValidException.class, () -> employeeService.creat(employee));
        verify(employeeRepository, never()).addEmployee(any());
    }

    @Test
    void should_throw_employee_age_and_salary_not_valid_exception_when_create_employee_given_employee_with_age_is_35_and_salary_is_3000() {
        // Given
        Employee employee = new Employee(1, "Tom", 35, Gender.MALE, 3000.0);
        // When
        // Then
        assertThrows(EmployeeSalaryNotValidException.class, () -> employeeService.creat(employee));
        verify(employeeRepository, never()).addEmployee(any());
    }

    @Test
    void should_employee_is_active_when_create_employee_given_a_employee() {
        // Given
        Employee employee = new Employee(1, "Tom", 35, Gender.MALE, 300000.0);
        when(employeeRepository.addEmployee(any())).thenReturn(employee);
        // When
        employeeService.creat(employee);
        // Then
        verify(employeeRepository).addEmployee(argThat(Employee::getActive));
    }

    @Test
    void should_throw_employee_inactive_exception_when_update_employee_given_a_inactive_employee() {
        // Given
        int id = 1;
        Employee employee = new Employee(1, "Tom", 35, Gender.MALE, 300000.0, false);
        // When
        when(employeeRepository.getEmployeeById(any())).thenReturn(employee);
        // Then
        assertThrows(EmployeeInactiveException.class, () -> employeeService.update(id, employee));
        verify(employeeRepository, never()).addEmployee(any());
    }

}

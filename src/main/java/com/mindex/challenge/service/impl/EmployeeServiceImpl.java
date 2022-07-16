package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
    //following setup from given things
    @Override
    public ReportingStructure report(String id) {
        LOG.debug("Creating reporting structure for employee with id [{}]", id);

        Employee employee = FindEmployee(id);
        int numberOfReports = GenNumberOfReports(employee);

        return new ReportingStructure(employee, numberOfReports);
    }

    @Override
    public Employee FindEmployee(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        return employee;
    }

    public int GenNumberOfReports(Employee employee) {
        List<Employee> CurReports = employee.getDirectReports();
        if (CurReports == null) /*need to start with 0*/ {
            return 0;
        }
        int RepSum = CurReports.size();
        for (Employee i : CurReports) {
            try {
                RepSum += GenNumberOfReports(FindEmployee(i.getEmployeeId()));
            } catch (RuntimeException ex) {
                //does nothing cause no employee found
            }
        }
        return RepSum;
    }
}

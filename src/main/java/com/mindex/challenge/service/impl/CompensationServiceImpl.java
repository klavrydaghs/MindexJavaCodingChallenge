package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private CompensationService compensationService;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        //need a random date for effective date in compensation
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        //need a random salary for compensation
        Random num = new Random();
        int salary = num.nextInt(999999);
        Employee employee = employeeService.create(compensation.getEmployee());
        compensation.setEmployee(employee);
        compensation.setSalary(String.format("%6d",salary));
        compensation.setEffectiveDate(formatter.format(date));
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation for employee with id [{}]", id);

        Employee employee = employeeService.FindEmployee(id);

        return findCompensation(employee);

    }

    @Override
    public Compensation findCompensation(Employee employee) {
        Compensation compensation = compensationRepository.findByEmployee(employee);
        if (compensation == null) {
            throw new RuntimeException("No compensation data for employeeId: " + employee.getEmployeeId());
        }
        return compensation;
    }
}
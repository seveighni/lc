package com.lc.application;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lc.application.model.Employee;
import com.lc.application.model.Office;
import com.lc.application.model.Rates;
import com.lc.application.model.Role;
import com.lc.application.model.User;
import com.lc.application.repository.EmployeeRepository;
import com.lc.application.repository.OfficeRepository;
import com.lc.application.repository.RatesRepository;
import com.lc.application.repository.RoleRepository;
import com.lc.application.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	OfficeRepository officeRepository;
	@Autowired
	private RatesRepository ratesRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		loadUserData();
	}

	private void loadUserData() {
		if (userRepository.count() == 0) {
			User admin = new User();
			admin.setEmail("admin@admin.com");
			admin.setFirstName("Admin");
			admin.setLastName("Admin");
			admin.setPassword(passwordEncoder.encode("admin"));
			var adminRole = roleRepository.findByName("ADMIN");
			if (adminRole == null) {
				adminRole = new Role();
				adminRole.setName("ADMIN");
			}
			admin.setRoles(Set.of(adminRole));
			userRepository.save(admin);

			User employeeUser = new User();
			employeeUser.setEmail("employee@test.com");
			employeeUser.setFirstName("employee");
			employeeUser.setLastName("employee");
			employeeUser.setPassword(passwordEncoder.encode("employee"));
			var employeeRole = roleRepository.findByName("EMPLOYEE");
			if (employeeRole == null) {
				employeeRole = new Role();
				employeeRole.setName("EMPLOYEE");
			}
			employeeUser.setRoles(Set.of(employeeRole));
			userRepository.save(employeeUser);
			Employee employee = new Employee();
			employee.setUser(employeeUser);
			employee.setActive(true);
			employeeRepository.save(employee);

			User userAwaitingApproval = new User();
			userAwaitingApproval.setFirstName("employeeToBe");
			userAwaitingApproval.setLastName("employeeToBe");
			userAwaitingApproval.setEmail("employeeToBe@test.com");
			userAwaitingApproval.setPassword(passwordEncoder.encode("employeeToBe"));
			userRepository.save(userAwaitingApproval);

			Office office = new Office();
			office.setAddress("Sofia, Blvd Bulgaria, 1");
			office.setIsActive(true);
			officeRepository.save(office);

			Rates rateForOffice = new Rates();
			rateForOffice.setName("ShipToOffice");
			rateForOffice.setPerKg(new BigDecimal(0.5));
			rateForOffice.setFlatRate(new BigDecimal(4.69));
			ratesRepository.save(rateForOffice);

			Rates rateForAddress = new Rates();
			rateForAddress.setName("ShipToAddress");
			rateForAddress.setPerKg(new BigDecimal(0.6));
			rateForAddress.setFlatRate(new BigDecimal(6.69));
			ratesRepository.save(rateForAddress);
		}
	}
}
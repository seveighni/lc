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
			User user = new User();
			user.setEmail("admin@admin.com");
			user.setFirstName("Admin");
			user.setLastName("Admin");
			user.setPassword(passwordEncoder.encode("admin"));
			var role = roleRepository.findByName("ADMIN");
			if (role == null) {
				role = new Role();
				role.setName("ADMIN");
			}
			user.setRoles(Set.of(role));
			userRepository.save(user);
			
			User user1 = new User();
			user1.setEmail("employee@test.com");
			user1.setFirstName("employee");
			user1.setLastName("employee");
			user1.setPassword(passwordEncoder.encode("employee"));
			var role1 = roleRepository.findByName("EMPLOYEE");
			if (role1 == null) {
				role1 = new Role();
				role1.setName("EMPLOYEE");
			}
			user1.setRoles(Set.of(role1));
			userRepository.save(user1);
			Employee employee = new Employee();
			employee.setUser(user1);
			employee.setActive(true);
			employeeRepository.save(employee);	
			
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
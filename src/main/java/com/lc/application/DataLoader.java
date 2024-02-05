package com.lc.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lc.application.model.Customer;
import com.lc.application.model.Employee;
import com.lc.application.model.Office;
import com.lc.application.model.Rates;
import com.lc.application.model.Role;
import com.lc.application.model.User;
import com.lc.application.repository.CustomerRepository;
import com.lc.application.repository.EmployeeRepository;
import com.lc.application.repository.OfficeRepository;
import com.lc.application.repository.RatesRepository;
import com.lc.application.repository.RoleRepository;
import com.lc.application.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class DataLoader  {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	OfficeRepository officeRepository;
	@Autowired
	private RatesRepository ratesRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@EventListener
	@Transactional
	public void loadUserData(ContextRefreshedEvent event) {
		if (userRepository.count() == 0) {

			var admin = createUser("Stefan", "Stefanov", "admin@test.com", "password");
			addRole(admin.getId(), "ADMIN");

			var employeeUser = createUser("Georgi", "Georgiev", "employee@test.com", "password");
			addRole(employeeUser.getId(), "EMPLOYEE");
			Employee employee = new Employee();
			employee.setUser(employeeUser);
			employee.setActive(true);
			employeeRepository.save(employee);

			var usr3 = createUser("Galya", "Galyova", "employeeToBe@test.com", "password");

			var customerUser = createUser("Tosho", "Toshov", "tosho@test.com", "password");
			addRole(customerUser.getId(), "CUSTOMER");
			Customer customer = new Customer();
			customer.setUser(customerUser);
			customerRepository.save(customer);

			var customerUserTwo = createUser("Ivan", "Ivanov", "ivan@test.com", "password");
			addRole(customerUserTwo.getId(), "CUSTOMER");
			Customer customerTwo = new Customer();
			customerTwo.setUser(customerUserTwo);
			customerRepository.save(customerTwo);

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

	private User createUser(String firstName, String lastName, String email, String password) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		return userRepository.save(user);
	}

	private void addRole(Long userId, String roleName) {
		var user = userRepository.findById(userId).get();
		var role = roleRepository.findByName(roleName);
		if (role == null) {
			role = new Role();
			role.setName(roleName);
			user.addRole(role);
			roleRepository.save(role);
		} else {
			user.addRole(role);
			userRepository.save(user);
		}
	}
}
package com.lc.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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

@Component
public class DataLoader implements CommandLineRunner {

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

	@Override
	public void run(String... args) throws Exception {
		loadUserData();
	}

	private void loadUserData() {
		if (userRepository.count() == 0) {

			createUser("Stefan", "Stefanov", "admin@test.com", "password", "ADMIN", true);

			User employeeUser = createUser("Georgi", "Georgiev", "employee@test.com", "password", "EMPLOYEE", true);
			Employee employee = new Employee();
			employee.setUser(employeeUser);
			employee.setActive(true);
			employeeRepository.saveAndFlush(employee);

			createUser("Galya", "Galyova", "employeeToBe@test.com", "password", "", false);

			User customerUser = createUser("Tosho", "Toshov", "tosho@test.com", "password", "CUSTOMER", true);
			Customer customer = new Customer();
			customer.setUser(customerUser);
			customerRepository.saveAndFlush(customer);

			// createSecondCustomer();

			Office office = new Office();
			office.setAddress("Sofia, Blvd Bulgaria, 1");
			office.setIsActive(true);
			officeRepository.saveAndFlush(office);

			Rates rateForOffice = new Rates();
			rateForOffice.setName("ShipToOffice");
			rateForOffice.setPerKg(new BigDecimal(0.5));
			rateForOffice.setFlatRate(new BigDecimal(4.69));
			ratesRepository.saveAndFlush(rateForOffice);

			Rates rateForAddress = new Rates();
			rateForAddress.setName("ShipToAddress");
			rateForAddress.setPerKg(new BigDecimal(0.6));
			rateForAddress.setFlatRate(new BigDecimal(6.69));
			ratesRepository.saveAndFlush(rateForAddress);
		}
	}

	private void createSecondCustomer() {
		User customerUserTwo = createUser("Ivan", "Ivanov", "ivan@test.com", "password", "CUSTOMER", true);
		Customer customerTwo = new Customer();
		customerTwo.setUser(customerUserTwo);
		customerRepository.saveAndFlush(customerTwo);
	}

	private User createUser(String firstName, String lastName, String email, String password, String roleName,
			boolean shoouldAddRole) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		if (shoouldAddRole) {
			var role = roleRepository.findByName(roleName);
			if (role == null) {
				role = new Role();
				role.setName(roleName);
				user.addRole(role);
				// roleRepository.save(role);
				roleRepository.saveAndFlush(role);
				return user;
			}
			user.addRole(role);
		}
		return userRepository.saveAndFlush(user);
	}
}
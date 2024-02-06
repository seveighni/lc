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
public class DataLoader {

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

			var admin = createUser("Stefan", "Kirilov", "admin@test.com", "password");
			addRole(admin.getId(), "ADMIN");

			createEmployee("Georgi", "Todorov", "employee1@test.com", "password", "Office", true);
			createEmployee("Gordan", "Ivanov", "employee2@test.com", "password", "Office", true);
			createEmployee("Grigor", "Toshov", "employee3@test.com", "password", "Delivery", true);
			createEmployee("Yasen", "Atanasov", "employee4@test.com", "password", "Office", false);

			createUser("Galya", "Dimitrova", "employeeToBe1@test.com", "password");
			createUser("Ivan", "Gabrielov", "employeeToBe2@test.com", "password");

			createCustomer("Tosho", "Grigorov", "cutomer1@test.com", "password");
			createCustomer("Ivan", "Asenov", "cutomer2@test.com", "password");
			createCustomer("Petar", "Danielov", "cutomer3@test.com", "password");
			createCustomer("Borislav", "Hristov", "cutomer4@test.com", "password");
			createCustomer("Viktor", "Petrov", "cutomer5@test.com", "password");
			createCustomer("Bojidar", "Yordanov", "cutomer6@test.com", "password");
			createCustomer("Borislav", "Borisov", "cutomer7@test.com", "password");
			createCustomer("Ani", "Tomova", "cutomer8@test.com", "password");
			createCustomer("Plamen", "Jivkov", "cutomer9@test.com", "password");
			createCustomer("Mario", "Kristianov", "cutomer10@test.com", "password");
			createCustomer("Asen", "Plamenov", "cutomer11@test.com", "password");
			createCustomer("Daniel", "Kosev", "cutomer12@test.com", "password");

			createOffice("Sofia, Blvd Bulgaria, 1", true);
			createOffice("Sofia, Blvd Todor Kableshkov, 34", true);
			createOffice("Sofia, Blvd Filip Kutev, 5", false);
			createOffice("Sofia, Blvd Cherni Vruh, 17", true);

			createRate("ShipToOffice", 0.5, 4.69);
			createRate("ShipToAddress", 0.6, 6.69);

			// TODO Parcels
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

	private void createEmployee(String firstName, String lastName, String email, String password, String type,
			boolean isActive) {
		var employeeUser = createUser(firstName, lastName, email, password);
		addRole(employeeUser.getId(), "EMPLOYEE");
		Employee employee = new Employee();
		employee.setUser(employeeUser);
		employee.setType(type);
		employee.setActive(isActive);
		employeeRepository.save(employee);
	}

	private void createCustomer(String firstName, String lastName, String email, String password) {
		var customerUser = createUser(firstName, lastName, email, password);
		addRole(customerUser.getId(), "CUSTOMER");
		Customer customer = new Customer();
		customer.setUser(customerUser);
		customerRepository.save(customer);
	}

	private void createOffice(String address, boolean isActive) {
		Office office = new Office();
		office.setAddress(address);
		office.setIsActive(isActive);
		officeRepository.save(office);
	}

	private void createRate(String name, double perKg, double flatRate) {
		Rates rateForOffice = new Rates();
		rateForOffice.setName(name);
		rateForOffice.setPerKg(new BigDecimal(perKg));
		rateForOffice.setFlatRate(new BigDecimal(flatRate));
		ratesRepository.save(rateForOffice);
	}
}
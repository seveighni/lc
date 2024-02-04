package com.lc.application;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lc.application.model.Role;
import com.lc.application.model.User;
import com.lc.application.repository.RoleRepository;
import com.lc.application.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
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
		}
		System.out.println(userRepository.count());
	}
}
package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userBob = new User("bob@builder.com", "b2020", "Bob", "Builder");
        userBob.addRole(roleAdmin);

        User savedUser = repo.save(userBob);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userMatt = new User("matt@gmail.com", "m2020", "matt", "beat");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(4);

        userMatt.addRole(roleEditor);
        userMatt.addRole(roleAssistant);

        User savedUser = repo.save(userMatt);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userBob = repo.findById(1).get();
        System.out.println(userBob);
        assertThat(userBob).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userBob = repo.findById(1).get();
        userBob.setEnabled(true);
        userBob.setEmail("bobBuilder@gmail.com");

        repo.save(userBob);
    }

    @Test
    public void testUpdateUserRoles() {
        User userMatt = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        userMatt.getRoles().remove(roleEditor);
        userMatt.addRole(roleSalesperson);

        repo.save(userMatt);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        repo.deleteById(userId);

    }
}

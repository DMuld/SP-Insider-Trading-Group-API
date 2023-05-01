package com.backend.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.backend.springboot.database.Trade;
import com.backend.springboot.database.TradeRepository;
import com.backend.springboot.database.User;
import com.backend.springboot.database.UserRepository;
import com.backend.springboot.handlers.TradeHandler;
import com.backend.springboot.handlers.UserHandler;

@SpringBootTest
@TestPropertySource(
		locations = "classpath:application-test.properties")
class ApplicationTests {
	
	@Autowired
	TradeRepository tradeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	Trade t1 = new Trade();
	Trade t2 = new Trade();
	
	User u1 = new User();
	User u2 = new User();
	
	@BeforeEach
	public void setupDB() {
		tradeRepository.deleteAll();
		userRepository.deleteAll();
		
		u1 = new User();
		u2 = new User();
		
		t1 = new Trade();
		t2 = new Trade();
		
		t1.setPRICE(1.0f);
		t1.setFILEDAFTER(1);
		t1.setPUBLISHED("1/1/2023");
		t1.setTRADED("1/2/2023");
		t1.setTICKER("TRADE1");
		t1.setTYPE("BUY");
		t1.setVOLUME(100);
		
		tradeRepository.save(t1);
		
		t2.setPRICE(2.0f);
		t2.setFILEDAFTER(2);
		t2.setPUBLISHED("1/1/2023");
		t2.setTRADED("1/3/2023");
		t2.setTICKER("TRADE2");
		t2.setTYPE("SELL");
		t2.setVOLUME(200);
		
		tradeRepository.save(t2);
		
		u1.setId(1);
		u1.setEmail("email1@email.com");
		u1.setPasswordHash("phash1");
		
		userRepository.save(u1);
		
		u2.setId(2);
		u2.setEmail("email2@email.com");
		u2.setPasswordHash("phash2");
		u2.setFavorites("TRADE1");
		
		userRepository.save(u2);
	}

	@Test
	public void testGetTrades() {
		TradeHandler result = new TradeHandler();
		
		String expected = "TRADE1!1/1/2023!1/2/2023!1!BUY!100!1.0!|"
				+ "TRADE2!1/1/2023!1/3/2023!2!SELL!200!2.0!|";
		
		assertThat(expected.equals(result.getTrades(tradeRepository)));
	}

	
	@Test
	public void testGetTradeByKeyword() {
		TradeHandler result = new TradeHandler();
		
		String expected = "TRADE1!1/1/2023!1/2/2023!1!BUY!100!1.0!|";
		
		assertThat(expected.equals(result.getTradeByKeyword(tradeRepository, "TRADE1")));
	}
	
	@Test
	public void testGetTradeByFavorites() {
		TradeHandler result = new TradeHandler();
		
		String expected = "TRADE1!1/1/2023!1/2/2023!1!BUY!100!1.0!|";
		
		assertThat(expected.equals(result.getTradeByFavorites(tradeRepository, "TRADE1,TRADE3")));
	}
	
	@Test
	public void testGetUser1() {
		UserHandler result = new UserHandler();
		
		String expected = "email1@email.com,email2@email.com!TRADE1,";
		
		assertThat(expected.equals(result.getUser("*", userRepository)));
	}
	
	@Test
	public void testGetUser2() {
		UserHandler result = new UserHandler();
		
		String expected = "email2@email.com!TRADE1";
		
		assertThat(expected.equals(result.getUser("email1@email.com", userRepository)));
	}
	
	@Test
	public void testAuthenticateUser() {
		UserHandler result = new UserHandler();
		
		String expected = "Authenticated";
		
		assertThat(expected.equals(result.authenticateUser("email1@email.com", "phash1", userRepository)));
	}
	
	@Test
	public void testAuthenticateUser2() {
		UserHandler result = new UserHandler();
		
		String expected = "";
		
		assertThat(expected.equals(result.authenticateUser("fakeuser", "fakephash", userRepository)));
	}
	
	@Test
	public void testCreateUser() {
		UserHandler result = new UserHandler();
		
		String expected = "Created user";
		String expected2 = "Authenticated";
		
		assertThat(expected.equals(result.createUser("email3@email.com", "phash3", userRepository)));
		assertThat(expected2.equals(result.authenticateUser("email3@email.com", "phash3", userRepository)));
	}
	
	@Test
	public void testUpdateFavorites() {
		UserHandler result = new UserHandler();
		User updatedU1 = new User();
		
		String expected = "Updated favorites";
		String newFavorites = "TEST1,TEST2";
		Iterable<User> allUsers = userRepository.findAll();
		
		assertThat(expected.equals(result.updateFavorites("email1@email.com", newFavorites, userRepository)));
		
		for (User u : allUsers) {
			if (u.getEmail().equals("email1@email.com")) {
				updatedU1 = u;
			}
		}
		
		assertThat(newFavorites.equals(updatedU1.getFavorites()));
	}
	
	@Test
	public void testUpdateUser() {
		UserHandler result = new UserHandler();
		User updatedU1 = new User();
		
		String expected = "User Password Changed";
		String newPhash = "phash3";
		Iterable<User> allUsers = userRepository.findAll();
		
		assertThat(expected.equals(result.updateUser("email1@email.com", newPhash, userRepository)));
		
		for (User u : allUsers) {
			if (u.getEmail().equals("email1@email.com")) {
				updatedU1 = u;
			}
		}
		
		assertThat(newPhash.equals(updatedU1.getPasswordHash()));
	}
}

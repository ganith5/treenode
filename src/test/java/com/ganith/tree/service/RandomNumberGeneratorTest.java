package com.ganith.tree.service;

import static org.junit.Assert.*;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import com.ganith.tree.service.RandomNumberGenerator;

@Slf4j
public class RandomNumberGeneratorTest {
	
	RandomNumberGenerator randomNumberGenerator;
	
	@Before
	public void init() {
		randomNumberGenerator = new RandomNumberGenerator(5, 10);
	}
	
	@Test
	public void generateRandomNumberInRange() {
		int random = randomNumberGenerator.generateRandomNumber();
		log.debug("Random number = {} ", random);
	}
	
	@Test
	public void generateRange() {
		int[] range = randomNumberGenerator.generateRandomRange();
		int min = range[0];
		int max = range[1];
		assertTrue((min < max));
	}

}

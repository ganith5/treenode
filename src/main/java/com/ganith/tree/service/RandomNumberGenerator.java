package com.ganith.tree.service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Class used to generate random integer range given a minimum and maximum
 * value. The random numbers are used to create factories
 * 
 * @author Pavithra
 * 
 */
public class RandomNumberGenerator {

	private int min;

	private int max;

	private int MIN_RANGE = 1;

	private int MAX_RANGE = 1000;

	public RandomNumberGenerator() {

	}

	public RandomNumberGenerator(int start, int end) {
		this.min = start;
		this.max = end;
	}

	public int generateRandomNumber() {
		ThreadLocalRandom r = ThreadLocalRandom.current();
		// Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	public int[] generateRandomRange() {
		int[] range = new int[2];
		range[0] = getMinRange();
		range[1] = getMaxRange(range[0]);
		return range;
	}

	private int getMaxRange(int minRange) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		int maxRandomNum = rand.nextInt((MAX_RANGE - minRange) + 1) + minRange;
		return maxRandomNum;
	}

	private int getMinRange() {
		// Random rand = new Random();
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		int minRandomNum = rand.nextInt((MAX_RANGE - MIN_RANGE) + 1)
				+ MIN_RANGE;
		return minRandomNum;
	}

}

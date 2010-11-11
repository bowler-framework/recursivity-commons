package com.recursivity.commons;
/*
 * Copyright (c) 2005 Wille Faler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import java.net.InetAddress;
import java.security.SecureRandom;

/**
 * @author Wille Faler, wille.faler@infonatural.com
 * 
 *         Object that implements a Universally Unique Identifier for use in
 *         primary-key generation for example. <br>
 *         The UUIDGenerators generated id:s have the following characteristics: <br>
 *         1. Unique down to the millisecond. <br>
 *         2. Unique across a cluster due to a portion generated on the basis of
 *         the underlying IP. <br>
 *         3. Unique down to the objects within a JVM. <br>
 *         4. Unique within an object within a millisecond due to randomization
 *         on each method-call.
 * 
 */
public class UUIDGenerator {

	private static UUIDGenerator generator = null;

	private SecureRandom seeder;

	private String midValue;

	/** Creates a new instance of UUIDGenerator */
	private UUIDGenerator() {
		try {
			InetAddress inet = InetAddress.getLocalHost();
			byte[] bytes = inet.getAddress();
			String hexInetAddress = hexFormat(getInt(bytes), 8);

			String thisHashCode = hexFormat(System.identityHashCode(this), 8);

			this.midValue = hexInetAddress + thisHashCode;
			seeder = new SecureRandom();
			int node = seeder.nextInt();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * gets an instance of the UUIDGenerator
	 */
	public static synchronized UUIDGenerator getInstance() {
		if (generator == null) {
			generator = new UUIDGenerator();
		}
		return generator;
	}

	/**
	 * generates a new unique id.
	 */
	private String generateInternal() {
		long timeNow = System.currentTimeMillis();

		int timeLow = (int) timeNow & 0xFFFFFFFF;
		int node = seeder.nextInt();
		return (hexFormat(timeLow, 8) + midValue + hexFormat(node, 8));
	}
	
	public static String generate(){
		return getInstance().generateInternal();
	}

	private static String hexFormat(int i, int j) {
		String s = Integer.toHexString(i);
		return padHex(s, j) + s;
	}

	private static String padHex(String s, int i) {
		StringBuffer tmpBuffer = new StringBuffer();
		if (s.length() < i) {
			for (int j = 0; j < i - s.length(); j++) {
				tmpBuffer.append('0');
			}
		}
		return tmpBuffer.toString();
	}

	private static int getInt(byte bytes[]) {
		int i = 0;
		int j = 24;
		for (int k = 0; j >= 0; k++) {
			int l = bytes[k] & 0xff;
			i += l << j;
			j -= 8;
		}
		return i;
	}

}
package com.factory.infuse.internal.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedLock {	
	private static Lock lock = new ReentrantLock(true);
	public static Lock getSharedLock() {
		return lock;
	}
}

package com.github.aqiu202.lock.base;

import java.util.concurrent.ConcurrentLinkedDeque;

public class LockOwnerHolder {

    private static final ThreadLocal<ConcurrentLinkedDeque<String>> LOCK_OWNER_THREAD_LOCAL
        = ThreadLocal.withInitial(ConcurrentLinkedDeque::new);

    public static String consumeLockOwner() {
        return LOCK_OWNER_THREAD_LOCAL.get().pollLast();
    }

    public static void clear() {
        LOCK_OWNER_THREAD_LOCAL.remove();
    }

    public static void addLockOwner(String lockOwner) {
        LOCK_OWNER_THREAD_LOCAL.get().addLast(lockOwner);
    }

}

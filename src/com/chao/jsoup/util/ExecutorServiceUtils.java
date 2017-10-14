package com.chao.jsoup.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 * Created by Chao on 2017/8/13.
 */
public class ExecutorServiceUtils {
    private static ExecutorServiceUtils instance;
    ExecutorService cachedThreadPool;

    private ExecutorServiceUtils() {
        initPool();
    }

    private void initPool() {
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    public static ExecutorServiceUtils getInstance() {
        if (instance == null) {
            synchronized (ExecutorServiceUtils.class) {
                if (instance == null) {
                    instance = new ExecutorServiceUtils();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable command) {
        cachedThreadPool.execute(command);
    }
}

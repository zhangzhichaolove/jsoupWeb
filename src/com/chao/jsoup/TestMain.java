package com.chao.jsoup;

import com.chao.jsoup.util.ExecutorServiceUtils;

/**
 * Created by 003 on 2018/3/28.
 */
public class TestMain {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ExecutorServiceUtils.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程：" + Thread.currentThread());
                }
            });
        }
    }
}

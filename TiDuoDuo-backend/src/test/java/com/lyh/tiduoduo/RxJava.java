package com.lyh.tiduoduo;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/31 22:02
 */
@SpringBootTest
public class RxJava {
    @Test
    public void test() throws InterruptedException {
        Flowable<Long> longFlowable = Flowable.interval(1, TimeUnit.SECONDS)
                .map(i -> i + 1)
                .subscribeOn(Schedulers.io());

        longFlowable.observeOn(Schedulers.io())
                .doOnNext(i -> System.out.println("doOnNext: " + i))
                .subscribe();

        Thread.sleep(10000);
    }
}

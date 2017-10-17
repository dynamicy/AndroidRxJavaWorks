package io.csie.chris.androidrxjavaworks;

import org.junit.Test;

import io.reactivex.Observable;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    String result = "";

    // Simple subscription to a fix value
    @Test
    public void returnAValue() {

        result = "";

        Observable<String> observer = Observable.just("Hello"); // provides datea
        observer.subscribe(s -> result = s); // Callable as subscriber
        assertTrue(result.equals("Hello"));
    }
}
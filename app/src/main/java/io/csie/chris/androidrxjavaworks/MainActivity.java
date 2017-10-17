package io.csie.chris.androidrxjavaworks;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    protected void onResume() {
        super.onResume();

//        sayHelloToTheWorld();

//        asyncExample();

        // 1->4->5->6->7->2->3->Done->8
        Log.d(TAG, "fromCallable: 1");
        Flowable<String> source = Flowable.fromCallable(() -> {
            Log.d(TAG, "fromCallable: 2");
            Thread.sleep(1000); //  imitate expensive computation
            Log.d(TAG, "fromCallable: 3");
            return "Done";
        });

        Log.d(TAG, "fromCallable: 4");

        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());

        Log.d(TAG, "fromCallable: 5");

        Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());

        Log.d(TAG, "fromCallable: 6");

        showForeground.subscribe(System.out::println, Throwable::printStackTrace);

        Log.d(TAG, "fromCallable: 7");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "fromCallable: 8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("MethodMayBeStatic")
    private void asyncExample() {
        // 3->1->2->Done->4
        Flowable.fromCallable(() -> {
            Log.d(TAG, "fromCallable: 1");
            Thread.sleep(2000); //  imitate expensive computation
            Log.d(TAG, "fromCallable: 2");
            return "Done";
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.single())
                .subscribe(System.out::println, Throwable::printStackTrace);

        Log.d(TAG, "fromCallable: 3");

        try {
            Thread.sleep(10000); // <--- wait for the flow to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "fromCallable: 4");
    }

    @SuppressWarnings("MethodMayBeStatic")
    private void sayHelloToTheWorld() {
        // Don't show anything
        Flowable.just("Hello world1");

        // Hello world2 shows
        Flowable.just("Hello world2").subscribe(System.out::println);

        // Without java8
        // Hello world3 shows
        Flowable.just("Hello world3")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s);
                    }
                });

        // Don't show anything
        Observable<String> myObservable = Observable.just("Hello world3");

        // Hello world4 shows
        Disposable myObservable1 = Observable.just("Hello world4").subscribe(System.out::println);

        // Hello world5 777 shows
        Disposable myObservable2 = Observable.just("Hello world5")
                .map(s -> s + " 777")
                .subscribe(System.out::println);

        Disposable myObservable4 = Observable.just("Hello world6")
                .map(String::hashCode)
                .subscribe(i -> System.out.println(Integer.toString(i)));

        Disposable myObservable5 = Observable.just("Hello world7")
                .map(String::hashCode)
                .map(i -> Integer.toString(i))
                .subscribe(System.out::println);

        Disposable myObservable6 = Observable.just("Hello world8")
                .map(s -> s + " 999")
                .map(String::hashCode)
                .map(i -> Integer.toString(i))
                .subscribe(System.out::println);
    }
}

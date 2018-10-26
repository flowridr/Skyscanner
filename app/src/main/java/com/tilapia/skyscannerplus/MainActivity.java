package com.tilapia.skyscannerplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tilapia.skyscannerplus.models.PollResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    SkyscannerAPI skyscannerAPI;
    final String APIKEY = "ss630745725358065467897349852985";
    final String STORE_KEY_POLLRESULT = "POLLRESULT_KEY";
    final int nrOfAdults = 1;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarDetailText = findViewById(R.id.toolbar_details_text);
        toolbarDetailText.setText(getString(R.string.toolbar_detail, stringFromDate(getGoingDate().getTime()), stringFromDate(getReturnDate(getGoingDate()).getTime()), nrOfAdults, "economy"));

        Paper.init(getApplicationContext());

        createSkyscannerAPI();

        progressBar = findViewById(R.id.indeterminateBar);

        Observable<Response<Void>> call = skyscannerAPI.createSession("GB", "GBP", "en-US", "EDI-sky", "LOND-sky", apiStringFromDate(getGoingDate().getTime()), apiStringFromDate(getReturnDate(getGoingDate()).getTime()), nrOfAdults, APIKEY);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        pollResults(voidResponse.headers().get("Location"));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void pollResults(String location){
        skyscannerAPI.pollResults(location, APIKEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(pollResponse -> {
                    if (pollResponse.code() == 304) {
                        setupAdapter(Paper.book().read(STORE_KEY_POLLRESULT));
                    } else {
                        Paper.book().write(STORE_KEY_POLLRESULT, pollResponse.body());
                        setupAdapter(pollResponse.body());
                    }
                })
                .doOnComplete(() -> progressBar.setVisibility(View.INVISIBLE))
                .repeatWhen(observable -> observable.delay(12, TimeUnit.MILLISECONDS))
                .takeUntil(r -> {
                    if(r.body() != null) {
                        return r.body().getStatus().equalsIgnoreCase("UpdatesComplete");
                    }
                    return true;
                })
                .subscribe();
    }

    private void setupAdapter(PollResult result){
        if (result == null) {
            return;
        }

        TextView resultCountTxt = findViewById(R.id.txt_results);

        resultCountTxt.setText(getString(R.string.result_count, result.getItineraries().size(), result.getItineraries().size()));

        RecyclerView recyclerView = findViewById(R.id.result_recyclerview);

        ResultAdapter adapter = new ResultAdapter(result);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void createSkyscannerAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SkyscannerAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        skyscannerAPI = retrofit.create(SkyscannerAPI.class);
    }

    private Calendar getGoingDate(){
        int dayOfWeek = Calendar.MONDAY;
        int hour      = 10; // 10 AM
        int minute    = 0;

        Calendar cal = Calendar.getInstance(); // Today, now
        if (cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            cal.add(Calendar.DAY_OF_MONTH, (dayOfWeek + 7 - cal.get(Calendar.DAY_OF_WEEK)) % 7);
        } else {
            int minOfDay = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
            if (minOfDay >= hour * 60 + minute)
                cal.add(Calendar.DAY_OF_MONTH, 7); // Bump to next week
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    private Calendar getReturnDate(Calendar cal){
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return cal;
    }

    private String stringFromDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        return formatter.format(date);
    }

    private String apiStringFromDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
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
}

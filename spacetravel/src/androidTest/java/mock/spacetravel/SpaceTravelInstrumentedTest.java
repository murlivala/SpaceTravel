package mock.spacetravel;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import mock.spacetravel.callbacks.ServiceCallback;
import mock.spacetravel.model.FlightModel;
import mock.spacetravel.model.ResponseModel;
import mock.spacetravel.service.SpaceTravelService;
import mock.spacetravel.ui.SpaceTravelActivity;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SpaceTravelInstrumentedTest {

    @Test
    public void TestAuthenticationWithValidKey() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        SpaceTravelService spaceTravelService = new SpaceTravelService(new ServiceCallback() {
            @Override
            public void onSuccess(int status, Response response) {
                Gson gson = new Gson();
                Type responseType = new TypeToken<ResponseModel>(){}.getType();
                ResponseModel result = gson.fromJson(response.body().charStream(), responseType);
                assertEquals("success",result.getStatus());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {

            }
        });
        spaceTravelService.authenticate(TestData.TEST_VALID_KEY);
        semaphore.acquire();
    }

    @Test
    public void TestAuthenticationWithInvalidKey() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        SpaceTravelService spaceTravelService = new SpaceTravelService(new ServiceCallback() {
            @Override
            public void onSuccess(int status, Response response) {
                Gson gson = new Gson();
                Type responseType = new TypeToken<ResponseModel>(){}.getType();
                ResponseModel result = gson.fromJson(response.body().charStream(), responseType);
                assertEquals("error",result.getStatus());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {

            }
        });
        spaceTravelService.authenticate(TestData.TEST_INVALID_KEY);
        semaphore.acquire();
    }


    @Test
    public void TestEmptyUserValidation() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        SpaceTravelService spaceTravelService = new SpaceTravelService(new ServiceCallback() {
            @Override
            public void onSuccess(int status, Response response) {
                Gson gson = new Gson();
                Type responseType = new TypeToken<ResponseModel>(){}.getType();
                ResponseModel result = gson.fromJson(response.body().charStream(), responseType);
                assertEquals("error",result.getStatus());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {

            }
        });
        spaceTravelService.validate(TestData.TEST_BLANK_EMAIL,TestData.TEST_BLANK_PASSWORD);
        semaphore.acquire();
    }

    @Test
    public void TestInvalidUserValidation() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        SpaceTravelService spaceTravelService = new SpaceTravelService(new ServiceCallback() {
            @Override
            public void onSuccess(int status, Response response) {
                Gson gson = new Gson();
                Type responseType = new TypeToken<ResponseModel>(){}.getType();
                ResponseModel result = gson.fromJson(response.body().charStream(), responseType);
                assertEquals("error",result.getStatus());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {

            }
        });
        spaceTravelService.validate(TestData.TEST_INVALID_EMAIL,TestData.TEST_INVALID_PASSWORD);
        semaphore.acquire();
    }

    @Test
    public void TestValidUserValidation() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        SpaceTravelService spaceTravelService = new SpaceTravelService(new ServiceCallback() {
            @Override
            public void onSuccess(int status, Response response) {
                Gson gson = new Gson();
                Type responseType = new TypeToken<ResponseModel>(){}.getType();
                ResponseModel result = gson.fromJson(response.body().charStream(), responseType);
                assertEquals("success",result.getStatus());
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {

            }
        });
        spaceTravelService.validate(TestData.TEST_VALID_EMAIL,TestData.TEST_VALID_PASSWORD);
        semaphore.acquire();
    }

    @Test
    public void TestAllFlights() throws Exception {
        final Semaphore semaphore = new Semaphore(0);
        SpaceTravelService spaceTravelService = new SpaceTravelService(new ServiceCallback() {
            @Override
            public void onSuccess(int status, Response response) {
                assertNotNull(response);
                semaphore.release();
            }

            @Override
            public void onFailure(int status, String errorMessage) {

            }
        });
        spaceTravelService.getAllFlights();
        semaphore.acquire();
    }

}

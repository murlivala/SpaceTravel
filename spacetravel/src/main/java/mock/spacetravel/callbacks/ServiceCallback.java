package mock.spacetravel.callbacks;

import okhttp3.Response;

/**
 * service callbacks
 */
public interface ServiceCallback {
    void onSuccess(int status, Response response);
    void onFailure(int status, String errorMessage);
}

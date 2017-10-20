package mock.spacetravel.callbacks;

import okhttp3.Response;

/**
 * callbacks to update ui
 */
public interface UICallback {
    void updateView(int status, String response);
    void onError(int status, String errMessage);
}

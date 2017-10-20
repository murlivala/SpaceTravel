package mock.spacetravel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model class for response from server
 */

public class ResponseModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }
}

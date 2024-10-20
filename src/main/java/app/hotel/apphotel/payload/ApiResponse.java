package app.hotel.apphotel.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String message;

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

    public ApiResponse(Integer status) {
        this.status = status;
    }

    public ApiResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    private Integer status;

    private Object data;


}

package congntph34559.fpoly.ph34559_ass_application.API;

import java.util.List;

import congntph34559.fpoly.ph34559_ass_application.DTO.ShoeDTO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {


//    192.168.1.161 API Home
//    10.24.21.223 API School

    String DOMAIN = "http://192.168.1.161:3000";

    @GET("/api/get-list-shoe")
    Call<List<ShoeDTO>> getShoe();


    @POST("/api/post-shoe")
    Call<ShoeDTO> createShoe(@Body ShoeDTO shoe);

    //DEMO
    @Multipart
    @POST("/api/post-shoe")
    Call<ShoeDTO> createShoe2(
            @Part("name") RequestBody name,
            @Part("brand") RequestBody brand,
            @Part("price") RequestBody price,
            @Part("size") RequestBody size,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT("/api/update-shoe-by-id/{id}")
    Call<ShoeDTO> updateShoe(@Path("id") String id,
                             @Part("name") RequestBody name,
                             @Part("brand") RequestBody brand,
                             @Part("price") RequestBody price,
                             @Part("size") RequestBody size,
                             @Part MultipartBody.Part image);

    @DELETE("/api/delete-shoe-by-id/{id}")
    Call<ShoeDTO> deleteShoe(@Path("id") String id);

}

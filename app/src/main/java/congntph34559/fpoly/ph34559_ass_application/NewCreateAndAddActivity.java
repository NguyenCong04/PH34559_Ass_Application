package congntph34559.fpoly.ph34559_ass_application;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import congntph34559.fpoly.ph34559_ass_application.API.APIService;
import congntph34559.fpoly.ph34559_ass_application.DTO.ShoeDTO;
import congntph34559.fpoly.ph34559_ass_application.Utils.RealPathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class NewCreateAndAddActivity extends AppCompatActivity {
    private static final int MY_RES_CODE = 10;
    TextView tvTitle;
    ImageView ivBack, ivImageShoe;
    Spinner spSize;
    ArrayAdapter adapterSp;
    List<String> listSize = new ArrayList<String>();
    AppCompatButton btnNewAndEdit;
    EditText edName, edPrice, edBrand;
    LinearLayout btnChooseImage;

    Uri mUri;//DEMO

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

            Log.e("zzzzz", "onActivityResult: ");

            if (o.getResultCode() == Activity.RESULT_OK) {
                Intent data = o.getData();
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ivImageShoe.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_create_and_add);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBackCreUp);
        spSize = findViewById(R.id.spSize);
        btnNewAndEdit = findViewById(R.id.btnNewAndEdit);
        edBrand = findViewById(R.id.edBrand);
        edName = findViewById(R.id.edName);
        edPrice = findViewById(R.id.edPrice);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        ivImageShoe = findViewById(R.id.ivImageShoe);

        //Cấu hình spinner
        Spinner();

        //Cầu hình UI
        ChangeUI();

        //Add shoe
        btnNewAndEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String titleAdd = getIntent().getStringExtra("titleAdd");
                //Data intent update
                if (titleAdd == null) {
                    UpdateShoe();
                } else {
//                    CreateShoe();
                    CreateShoe2();//DEMO

                }

            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });


    }

    @SuppressLint("ObsoleteSdkInt")
    private void ChooseImage() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_RES_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_RES_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }

    }

    private void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));


    }

    //DEMO
    private void CreateShoe2() {

        String name = edName.getText().toString();
        String brand = edBrand.getText().toString();
        String price = edPrice.getText().toString();
        String size = (String) spSize.getSelectedItem();

        if (mUri != null) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.DOMAIN).addConverterFactory(GsonConverterFactory.create()).build();

            APIService apiService = retrofit.create(APIService.class);


            String strRealPath = RealPathUtil.getRealPath(this, mUri);
            Log.e("xxxxxxx", "CreateShoe2: " + strRealPath);
            File file = new File(strRealPath);

            RequestBody reqName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody reqBrand = RequestBody.create(MediaType.parse("multipart/form-data"), brand);
            RequestBody reqPrice = RequestBody.create(MediaType.parse("multipart/form-data"), price);
            RequestBody reqSize = RequestBody.create(MediaType.parse("multipart/form-data"), size);
            RequestBody requestBodyUri = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part mPart = MultipartBody.Part.createFormData("image", file.getName(), requestBodyUri);

            Log.e("xxxxxx", "CreateShoe2: " + mPart);
            Log.e("xxxxxx", "CreateShoe2: " + file);

            Call<ShoeDTO> call = apiService.createShoe2(reqName, reqBrand, reqPrice, reqSize, mPart);

            call.enqueue(new Callback<ShoeDTO>() {
                @Override
                public void onResponse(Call<ShoeDTO> call, Response<ShoeDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(NewCreateAndAddActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewCreateAndAddActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ShoeDTO> call, Throwable t) {
                    Log.e("zzzzz", "onFailure: " + t.getMessage());
                }
            });


        }


    }

    private boolean CheckCreateShoe() {

        return true;
    }

    private void UpdateShoe() {

        String name = edName.getText().toString();
        String brand = edBrand.getText().toString();
        String price = edPrice.getText().toString();
        String size = (String) spSize.getSelectedItem();
        String id = getIntent().getStringExtra("id");

        if (CheckCreateShoe()) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.DOMAIN).addConverterFactory(GsonConverterFactory.create()).build();

            APIService apiService = retrofit.create(APIService.class);
            String strRealPath = RealPathUtil.getRealPath(this, mUri);
            Log.e("xxxxxxx", "CreateShoe2: " + strRealPath);
            File file = new File(strRealPath);

            RequestBody reqName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody reqBrand = RequestBody.create(MediaType.parse("multipart/form-data"), brand);
            RequestBody reqPrice = RequestBody.create(MediaType.parse("multipart/form-data"), price);
            RequestBody reqSize = RequestBody.create(MediaType.parse("multipart/form-data"), size);
            RequestBody requestBodyUri = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part mPart = MultipartBody.Part.createFormData("image", file.getName(), requestBodyUri);

            Call<ShoeDTO> call = apiService.updateShoe(id, reqName, reqBrand, reqPrice, reqSize, mPart);


            call.enqueue(new Callback<ShoeDTO>() {
                @Override
                public void onResponse(Call<ShoeDTO> call, Response<ShoeDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(NewCreateAndAddActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewCreateAndAddActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ShoeDTO> call, Throwable t) {
                    Log.e("zzzzz", "onFailure: " + t.getMessage());
                }
            });


        }


    }


    private void ChangeUI() {
        //Data intent add
        String titleAdd = getIntent().getStringExtra("titleAdd");
        String titleBtnAdd = getIntent().getStringExtra("titleBtnAdd");
        //Data intent update
        String titleUpdate = getIntent().getStringExtra("titleEdit");
        String titleBtnUp = getIntent().getStringExtra("titleBtnEdit");
        String name = getIntent().getStringExtra("name");
        String brand = getIntent().getStringExtra("brand");
        Long price = getIntent().getLongExtra("price", 0);
        String size = getIntent().getStringExtra("size");
        String image = getIntent().getStringExtra("image");

        if (titleUpdate == null) {
            tvTitle.setText(titleAdd);
            btnNewAndEdit.setText(titleBtnAdd);
        } else {
            tvTitle.setText(titleUpdate);
            edName.setText(name);
            edBrand.setText(brand);
            edPrice.setText(price + "");
            int viTri = 0;
            for (int i = 0; i < listSize.size(); i++) {
                if (size.equals(listSize.get(i).toString())) {
                    viTri = i;
                    break;
                }
            }
            spSize.setSelection(viTri);
            Glide.with(this).load(image).into(ivImageShoe);
            btnNewAndEdit.setText(titleBtnUp);
        }

        //set onClick nut back
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewCreateAndAddActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void Spinner() {

        listSize.add("S");
        listSize.add("M");
        listSize.add("L");
        listSize.add("XL");
        listSize.add("XXL");

        adapterSp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listSize);
        spSize.setAdapter(adapterSp);

    }
}
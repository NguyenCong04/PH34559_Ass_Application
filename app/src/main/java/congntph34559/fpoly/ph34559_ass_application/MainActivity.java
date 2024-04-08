package congntph34559.fpoly.ph34559_ass_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import congntph34559.fpoly.ph34559_ass_application.API.APIService;
import congntph34559.fpoly.ph34559_ass_application.Adapter.AdapterShoe;
import congntph34559.fpoly.ph34559_ass_application.DTO.ShoeDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    static List<ShoeDTO> list = new ArrayList<>();
    static AdapterShoe adapterShoe;
    static RecyclerView recyclerView;
    FloatingActionButton floaAdd;
    EditText edSearch;
    TextView tvSortTang, tvSortGiam;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rcvList);
        floaAdd = findViewById(R.id.floatAdd);
        edSearch = findViewById(R.id.edSearch);
        tvSortGiam = findViewById(R.id.tvSapXepGiam);
        tvSortTang = findViewById(R.id.tvSapXepTang);


        //Connect
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Call Api Retrofit
        CallAPI(retrofit);


        //setOnclick add
        floaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewCreateAndAddActivity.class);
                intent.putExtra("titleAdd", "Create shoe");
                intent.putExtra("titleBtnAdd", "Create");
                startActivity(intent);
                finish();
            }
        });

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = edSearch.getText().toString().trim();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(APIService.DOMAIN)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    APIService apiService = retrofit.create(APIService.class);

                    apiService.searchShoe(key).enqueue(new Callback<List<ShoeDTO>>() {
                        @Override
                        public void onResponse(Call<List<ShoeDTO>> call, Response<List<ShoeDTO>> response) {
                            if (response.isSuccessful()) {
                                List<ShoeDTO> list1 = response.body();
                                getDs(list1);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ShoeDTO>> call, Throwable t) {
                            Log.e("zzzzzzzzzz", "onFailure: " + t.getMessage());
                        }
                    });
                    return true;
                }
                return false;
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(APIService.DOMAIN)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    //Call Api Retrofit
                    CallAPI(retrofit);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvSortGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoeTang();
            }
        });
        tvSortTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoe();
            }
        });

    }

    private void sortShoeTang() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);



        apiService.getSort().enqueue(new Callback<List<ShoeDTO>>() {
            @Override
            public void onResponse(Call<List<ShoeDTO>> call, Response<List<ShoeDTO>> response) {
                Log.e("zzzzz", "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    List<ShoeDTO> list1 = response.body();
                    getDs(list1);
                }
            }

            @Override
            public void onFailure(Call<List<ShoeDTO>> call, Throwable t) {
                Log.e("zzzzzzzzzz", "onFailure: " + t.getMessage());
            }
        });

    }


    public static void CallAPI(Retrofit retrofit) {

        //Khai báo API Service
        APIService apiService = retrofit.create(APIService.class);

        Call<List<ShoeDTO>> call = apiService.getShoe();

        call.enqueue(new Callback<List<ShoeDTO>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ShoeDTO>> call, @NonNull Response<List<ShoeDTO>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    getDs(list);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ShoeDTO>> call, @NonNull Throwable t) {
                Log.e("zzzz", "onFailure: " + t.getMessage());
            }
        });

    }

    private void sortShoe() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);



        apiService.getSortGiam().enqueue(new Callback<List<ShoeDTO>>() {
            @Override
            public void onResponse(Call<List<ShoeDTO>> call, Response<List<ShoeDTO>> response) {
                Log.e("zzzzz", "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    List<ShoeDTO> list1 = response.body();
                    getDs(list1);
                }
            }

            @Override
            public void onFailure(Call<List<ShoeDTO>> call, Throwable t) {
                Log.e("zzzzzzzzzz", "onFailure: " + t.getMessage());
            }
        });

    }

    private static void getDs(List<ShoeDTO> list) {

        adapterShoe = new AdapterShoe(recyclerView.getContext(), list);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterShoe);
        adapterShoe.notifyDataSetChanged();

    }
}
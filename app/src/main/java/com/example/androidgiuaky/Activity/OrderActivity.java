package com.example.androidgiuaky.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgiuaky.Adapter.OrderAdapter;
import com.example.androidgiuaky.Model.Order;
import com.example.androidgiuaky.Model.Product;
import com.example.androidgiuaky.Model.User;
import com.example.androidgiuaky.R;
import com.example.androidgiuaky.Retrofit.ApiService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    ListView lvDanhSach;
    OrderAdapter adapter;
    ImageView ivPreviousOder;
    Spinner spinnerFilterOrder;
    List<Order> orderList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    List<Order> orderList_tmp = new ArrayList<>();
    List<Order> filteredOrders = new ArrayList<>();
    String[] orderStatuses = {"ALL", "PREPARE" , "SUCCESS", "CANCELED"};
    int userID = 10101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setControl();
        getListProductAPI();

    }

    private void setEvent() {

        // lọc bỏ CART
        for (Order order : orderList_tmp) {
            if (!order.getOrderStatus().equals("CART")) {
                orderList.add(order);
            }
        }

        /// set Adapter cho Spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderStatuses);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterOrder.setAdapter(adapterSpinner);
        spinnerFilterOrder.setSelection(0);
        filteredOrders.addAll(orderList);
        spinnerFilterOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = orderStatuses[position];
                filteredOrders.clear();
                if (selectedStatus.equals("ALL")) {
                    filteredOrders.addAll(orderList);
                } else {
                    for (Order order : orderList) {
                        if (order.getOrderStatus().equals(selectedStatus)) {
                            filteredOrders.add(order);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //set Adapter cho orderList
        adapter = new OrderAdapter(OrderActivity.this, filteredOrders, productList);
        lvDanhSach.setAdapter(adapter);
        lvDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order = filteredOrders.get(i);
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("keyOrder", order);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) productList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ivPreviousOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void setControl() {
        lvDanhSach = findViewById(R.id.lvDanhSach);
        spinnerFilterOrder = findViewById(R.id.spinnerFilterOrder);
        ivPreviousOder = findViewById(R.id.ivPreviousOder);
    }
    private void getListProductAPI() {
        ApiService.apiService.productListData().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                System.out.println("ProductList Call API ok " + response.code());
                getOrdeByUserID();
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("ProductList Call API Errol  " + t);
            }
        });
    }
    private void getOrdeByUserID(){
        ApiService.apiService.getOrdersfromUserId(userID).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                orderList = response.body();
                System.out.println("orderlist Call API ok " + response.code());
                setEvent();
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }

}
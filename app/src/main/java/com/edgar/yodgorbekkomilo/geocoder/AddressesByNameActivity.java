package com.edgar.yodgorbekkomilo.geocoder;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AddressesByNameActivity extends AppCompatActivity {

    private AddressesByNameActivity.AddressListResultReceiver addressResultReceiver;

    private TextInputEditText addressNameTv;
    private ListView addressListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_addresses_location_layout);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setSubtitle("Addresses By Name");

        addressNameTv = findViewById(R.id.addresses_t);
        addressListView = findViewById(R.id.addresses_lst);

        addressResultReceiver = new AddressListResultReceiver(new Handler());

    }

    public void getAddressesByName(View view){
        getAddresses(addressNameTv.getText().toString());
    }

    private void getAddresses(String addName) {
        if (!Geocoder.isPresent()) {
            Toast.makeText(AddressesByNameActivity.this,
                    "Can't find address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, AddressesByNameIntentService.class);
        intent.putExtra("address_receiver", addressResultReceiver);
        intent.putExtra("address_name", addName);
        startService(intent);
    }

    private class AddressListResultReceiver extends ResultReceiver {
        AddressListResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                Toast.makeText(AddressesByNameActivity.this,
                        "Enter address name, " ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (resultCode == 1) {
                Toast.makeText(AddressesByNameActivity.this,
                        "Address not found, " ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String[] addressList = resultData.getStringArray("addressList");
            showResults(addressList);
        }
    }

    private void showResults(String[] addressList){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(arrayAdapter);
    }
}
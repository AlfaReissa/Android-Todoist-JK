package com.readthym.doesapp.ui.new_task;

import static org.koin.java.KoinJavaComponent.inject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.henrylabs.qumparan.data.remote.QumparanResource;
import com.readthym.doesapp.R;
import com.readthym.doesapp.data.local.MyPreference;
import com.readthym.doesapp.databinding.ActivityNewTaskBinding;
import com.readthym.doesapp.ui.home.HomeViewModel;
import com.readthym.doesapp.ui.home.MainTaskAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.Lazy;

public class NewTaskActivity extends AppCompatActivity {

    ActivityNewTaskBinding binding;
    MainTaskAdapter adapter;
    private Lazy<HomeViewModel> homeViewModel = inject(HomeViewModel.class);
    Calendar date;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new MainTaskAdapter();

        initView();
        initData();
    }

    private void initData() {
        homeViewModel.getValue().getSaveTaskLiveData().observe(this, data -> {
            if (data instanceof QumparanResource.Loading) {
                showLoading(true);
            }
            if (data instanceof QumparanResource.Success) {
                showLoading(false);
                if (data.getData().getStatusCode() == 1) {
                    finish();
                    onBackPressed();
                    showToast(NewTaskActivity.this,getString(R.string.berhasil_input_data));
                }else{
                    showToast(NewTaskActivity.this,getString(R.string.gagal_input_data));
                }
            }
            if (data instanceof QumparanResource.Error) {
                showLoading(false);
                showToast(NewTaskActivity.this, data.getMessage().toString());
            }
        });
    }



    private void showLoading(boolean b) {
        if(b){
            binding.loadingIndicator.setVisibility(View.VISIBLE);
        }else{
            binding.loadingIndicator.setVisibility(View.GONE);
        }
    }

    private void initView() {

        binding.btnCancel.setOnClickListener(view->{
            onBackPressed();
        });

        adapter.setAdapterInterface(model -> {

        });

        binding.datedoes.setOnClickListener(view -> {
            showDateTimePicker();
        });

        binding.btnSaveTask.setOnClickListener(view -> {
            boolean isError = false;

            String deadline = binding.datedoes.getText().toString();
            String title = binding.titledoes.getText().toString();
            String desc = binding.descdoes.getText().toString();

            if (deadline.equals(getString(R.string.klik_untuk_pilih_tanggal))) {
                isError = true;
            }

            if (title.isEmpty()) {
                isError = true;
            }

            if (desc.isEmpty()) {
                isError = true;
            }

            if (!isError) {
                String time = String.valueOf(date.getTimeInMillis());
                homeViewModel.getValue().saveTask(
                        getUserId(), title, desc, time
                );
            } else {
                showToast(NewTaskActivity.this, getString(R.string.fix_input_first));
            }

        });


    }

    private String getUserId() {
        return new MyPreference(this).getUserID();
    }

    public void showDateTimePicker() {
        Context ctx = this;
        final Calendar currentDate = Calendar.getInstance();
        date = currentDate;
        new DatePickerDialog(ctx, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(ctx, (timePicker, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                binding.datedoes.setText(getConvertedDateTime(date));
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private String getConvertedDateTime(Calendar calendar) {
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
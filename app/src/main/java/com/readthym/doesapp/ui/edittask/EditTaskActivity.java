package com.readthym.doesapp.ui.edittask;

import static org.koin.java.KoinJavaComponent.inject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.henrylabs.qumparan.data.remote.QumparanResource;
import com.readthym.doesapp.R;
import com.readthym.doesapp.data.remote.reqres.DetailTaskResponse;
import com.readthym.doesapp.databinding.ActivityNewTaskBinding;
import com.readthym.doesapp.ui.home.HomeViewModel;
import com.readthym.doesapp.ui.home.MainTaskAdapter;
import com.readthym.doesapp.utils.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.Lazy;

public class EditTaskActivity extends AppCompatActivity {

    ActivityNewTaskBinding binding;
    MainTaskAdapter adapter;
    private Lazy<HomeViewModel> homeViewModel = inject(HomeViewModel.class);
    Calendar date;
    Calendar currentDate;

    boolean isDateChange = false;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Calendar currentDate = Calendar.getInstance();
        date = currentDate;
        adapter = new MainTaskAdapter();

        initView();
        initData();
    }

    private void initData() {
        String taskID = getIntent().getStringExtra("taskID").toString();
        homeViewModel.getValue().getDetailTask(taskID);

        homeViewModel.getValue().getDetailTaskLiveData().observe(this, data -> {
            if (data instanceof QumparanResource.Loading) {
                showLoading(true);
            }
            if (data instanceof QumparanResource.Success) {
                showLoading(false);
                if (data.getData().getStatusCode() == 1) {
                    renderData(data.getData().getResData());
                } else {
                    showToast(EditTaskActivity.this, getString(R.string.gagal_menampilkan_data));
                }
            }
            if (data instanceof QumparanResource.Error) {
                showLoading(false);
                showToast(EditTaskActivity.this, data.getMessage().toString());
            }
        });

        homeViewModel.getValue().getSaveTaskLiveData().observe(this, data -> {
            if (data instanceof QumparanResource.Loading) {
                showLoading(true);
            }
            if (data instanceof QumparanResource.Success) {
                showLoading(false);
                if (data.getData().getStatusCode() == 1) {
                    finish();
                    onBackPressed();
                    showToast(EditTaskActivity.this, "Berhasil Update Data");
                } else {
                    showToast(EditTaskActivity.this, "Gagal Update Data");
                }
            }
            if (data instanceof QumparanResource.Error) {
                showLoading(false);
                showToast(EditTaskActivity.this, data.getMessage().toString());
            }
        });
    }

    private void renderData(DetailTaskResponse.ResData resData) {
        binding.datedoes.setText(new DateUtils().getReadableDate(resData.getDeadline()));
        binding.titledoes.setText(resData.getTitle());
        binding.descdoes.setText(resData.getDesc());
        binding.doesid.setText(String.valueOf(resData.getId()));
        binding.originalunix.setText((resData.getDeadline()));
    }

    private void showLoading(boolean b) {
        if (b) {
            binding.loadingIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.loadingIndicator.setVisibility(View.GONE);
        }
    }

    private void initView() {

        binding.btnCancel.setOnClickListener(view -> {
            onBackPressed();
        });

        adapter.setAdapterInterface(model -> {

        });

        binding.datedoes.setOnClickListener(view -> {
            showToast(EditTaskActivity.this,getString(R.string.tanggal_tidak_boleh_dupdate));
        });

        binding.btnSaveTask.setOnClickListener(view -> {
            boolean isError = false;

            String deadline = binding.datedoes.getText().toString();
            String title = binding.titledoes.getText().toString();
            String desc = binding.descdoes.getText().toString();
            String id = binding.doesid.getText().toString();

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
                if (isDateChange){
                    time = binding.datenewUnix.getText().toString();
                }else{
                    time = binding.originalunix.getText().toString();
                }
                homeViewModel.getValue().updateTask(
                        id, title, desc, time
                );
            } else {
                showToast(EditTaskActivity.this, getString(R.string.fix_input_first));
            }

        });


    }

    public void showDateTimePicker() {
        Context ctx = this;
        final Calendar currentDate = Calendar.getInstance();
        Calendar mydate = currentDate;
        new DatePickerDialog(ctx, (view, year, monthOfYear, dayOfMonth) -> {
            new TimePickerDialog(ctx, (timePicker, hourOfDay, minute) -> {
                mydate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mydate.set(Calendar.MINUTE, minute);
                binding.datedoes.setText(getConvertedDateTime(mydate));
                binding.datenewUnix.setText(String.valueOf(mydate.getTimeInMillis()));
                isDateChange = true;
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private String getConvertedDateTime(Calendar calendar) {
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
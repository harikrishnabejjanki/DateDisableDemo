package devrabbit.com.datedisabledemo;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.timessquare.CalendarPickerView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView datePickerViewTV;
    private GoogleApiClient client;
    private TextView timePickerViewTV;
    private String format = "";
    private TextView multiplePickerViewTV;
    private CalendarPickerView calendar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePickerViewTV = (TextView) findViewById(R.id.datePickerViewTV);
        timePickerViewTV = (TextView) findViewById(R.id.timePickerViewTV);
        multiplePickerViewTV = (TextView) findViewById(R.id.multiplePickerViewTV);
        timePickerViewTV.setOnClickListener(this);
        datePickerViewTV.setOnClickListener(this);
        multiplePickerViewTV.setOnClickListener(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        Log.v("Short name", Calendar.getInstance().getTimeZone().getDisplayName().toUpperCase());
        calendar_view.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
        List<Date> dates = calendar_view.getSelectedDates();
        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Toast.makeText(getApplicationContext(), "Selected Date is : " + date.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDateUnselected(Date date) {

                Toast.makeText(getApplicationContext(), "UnSelected Date is : " + date.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        timePickerViewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE), false
                );

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.datePickerViewTV) {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    MainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setDisabledDays(selectedDays());
            dpd.show(getFragmentManager(), "Datepickerdialog");

        } else if (R.id.multiplePickerViewTV == view.getId()) {
            for (int i = 0; i < calendar_view.getSelectedDates().size(); i++) {

                Toast.makeText(getApplicationContext(), calendar_view.getSelectedDates().get(i).toString(), Toast.LENGTH_SHORT).show();

            }

        }

    }

    private Calendar[] selectedDays() {
        ArrayList<Calendar> days = new ArrayList<>();
        Calendar[] originaldays = new Calendar[0];
        int[] serverselectedate = new int[]{2, 4, 17, 21, 19, 11, 14, 26, 31};
        try {
            for (int i = 0; i < serverselectedate.length; ++i) {
                Calendar selectedday = Calendar.getInstance();
                selectedday.add(Calendar.DAY_OF_MONTH, serverselectedate[i]-1);
                days.add(selectedday);
            }
            originaldays = new Calendar[days.size()];
            for (int j = 0; j < days.size(); ++j) {
                originaldays[j] = days.get(j);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originaldays;



    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear) + "/" + year;
        datePickerViewTV.setText(date);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();


    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        showTime(hourOfDay, minute);
    }

    public void showTime(int hour, int min) {

        if (hour == 0) {
            hour = hour + 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour = hour - 12;
            format = "PM";
        } else {
            format = "AM";
        }

        timePickerViewTV.setText(new StringBuilder().append(hour).append(" : ").append(min).append(" ").append(format));
    }
}

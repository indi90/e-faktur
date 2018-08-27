package id.co.jst.e_fakturjst;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.jst.e_fakturjst.adapter.ScanAdapter;
import id.co.jst.e_fakturjst.model.EmailCallback;
import id.co.jst.e_fakturjst.model.Scan;
import id.co.jst.e_fakturjst.model.SubmitCallback;
import id.co.jst.e_fakturjst.rest.ApiClient;
import id.co.jst.e_fakturjst.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    public Button btn_scan, btn_email;
    public TextView tot_scan, tot_ppn;
    public Switch is_credit;
    private RecyclerView recyclerView;
    private ScanAdapter adapter;
    private ArrayList<Scan> scanArrayList = new ArrayList<Scan>();
    private int sw_iscredit;
    private double total_ppn;
    private ProgressDialog pdg;

    private DecimalFormat kursIndonesia;
    private DecimalFormatSymbols formatRp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_scan = (Button)findViewById(R.id.button);
        btn_email = (Button)findViewById(R.id.button2);
        is_credit = (Switch) findViewById(R.id.switch1);
        tot_scan = (TextView) findViewById(R.id.textView3);
        tot_ppn = (TextView) findViewById(R.id.textView5);

        kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();

        formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
//        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        total_ppn = 0;

        pdg = new ProgressDialog(MainActivity.this);
        pdg.setIndeterminate(true);
        pdg.setInverseBackgroundForced(false);
        pdg.setCanceledOnTouchOutside(true);
        pdg.setMessage("Please Wait ...");
        pdg.setCancelable(false);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, 0);
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void confirm(){
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.confirm_form, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        alertDialogBuilder.setView(promptsView);

        final EditText et_month = (EditText) promptsView
                .findViewById(R.id.edittext);
        final EditText et_year = (EditText) promptsView
                .findViewById(R.id.edittext1);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                int month = Integer.valueOf(et_month.getText().toString());
                                int year = Integer.valueOf(et_year.getText().toString());
                                mail(month, year);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void mail(int month, int year) {
        Call<EmailCallback> callmaterial = apiService.email(month, year);
        pdg.show();
        callmaterial.enqueue(new Callback<EmailCallback>() {
            @Override
            public void onResponse(Call<EmailCallback> call, Response<EmailCallback> response) {
                try {
                    pdg.dismiss();
                    if (response.isSuccessful()){
                        if (response.body().getError_code() == 0){
                            Toast.makeText(MainActivity.this, response.body().getError_msg(), Toast.LENGTH_LONG).show();
                            recreate();
                        } else {
                            Toast.makeText(MainActivity.this, response.body().getError_msg(), Toast.LENGTH_LONG).show();
                            Log.d("error1", String.valueOf(response.body().getError_msg()));
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Not Success", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EmailCallback> call, Throwable t) {
                pdg.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("error", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode < 0) {
            String content = intent.getStringExtra("SCAN_RESULT");
            switch (requestCode) {
                case 0:
                    if (is_credit.isChecked()){
                        sw_iscredit = 1;
                    } else {
                        sw_iscredit = 0;
                    }
                    Log.d("Endro1", String.valueOf(sw_iscredit));
                    hasilscan(content, sw_iscredit);
                    break;
            }
        } else {
            Toast.makeText(MainActivity.this, "Cancel scan by user", Toast.LENGTH_LONG).show();
        }

    }

    private void addData(Scan dataScan){
//        scanArrayList = new ArrayList<>();

        this.total_ppn = this.total_ppn + Double.valueOf(dataScan.getTotal_ppn());

        Log.d("Endro3", String.valueOf(this.total_ppn));
        Log.d("Endro4", String.valueOf(scanArrayList.size()));

        scanArrayList.add(0, dataScan);
        Log.d("Endro4", String.valueOf(scanArrayList.size()));
        tot_scan.setText(String.valueOf(scanArrayList.size()));
        tot_ppn.setText(kursIndonesia.format(this.total_ppn));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ScanAdapter(scanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void hasilscan(String barcode, int credit) {
        Log.d("Endro2", String.valueOf(credit));
        Call<SubmitCallback> callsubmit = apiService.submit(barcode, credit);
        pdg.show();
        callsubmit.enqueue(new Callback<SubmitCallback>() {
            @Override
            public void onResponse(Call<SubmitCallback> call, Response<SubmitCallback> response) {
                try {
                    pdg.dismiss();
                    if (response.isSuccessful()){
                        if (response.body().getError_code() == 0){
                            Toast.makeText(MainActivity.this, response.body().getError_msg(), Toast.LENGTH_LONG).show();
                            addData(response.body().getData());
                        } else {
                            Toast.makeText(MainActivity.this, response.body().getError_msg(), Toast.LENGTH_LONG).show();
                            Log.d("error1", String.valueOf(response.body().getError_msg()));
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Not Success", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubmitCallback> call, Throwable t) {
                pdg.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("error", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
            .setMessage("Are you sure want to close the application ?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.this.finish();
                }
            })
            .setNegativeButton("No", null)

            .show();
    }
}

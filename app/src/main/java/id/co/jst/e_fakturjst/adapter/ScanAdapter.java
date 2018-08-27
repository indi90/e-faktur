package id.co.jst.e_fakturjst.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import id.co.jst.e_fakturjst.R;
import id.co.jst.e_fakturjst.model.Scan;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ScanViewHolder> {
    private ArrayList<Scan> dataList;

    public ScanAdapter(ArrayList<Scan> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ScanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScanViewHolder holder, int position) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
//        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        holder.faktur_no.setText("No. Faktur      : "+dataList.get(position).getFaktur_no());
        holder.npwp.setText("NPWP             : "+dataList.get(position).getNpwp());
        holder.name.setText("Perusahaan    : "+dataList.get(position).getName());
        holder.tot_ppn.setText("PPN                : "+kursIndonesia.format(Double.valueOf(dataList.get(position).getTotal_ppn())));
        holder.status_faktur.setText("Status Faktur : "+dataList.get(position).getStatus_faktur());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ScanViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur_no, npwp, name, tot_ppn, status_faktur;

        public ScanViewHolder(View itemView) {
            super(itemView);
            faktur_no = (TextView) itemView.findViewById(R.id.faktur_no);
            npwp = (TextView) itemView.findViewById(R.id.npwp);
            name = (TextView) itemView.findViewById(R.id.name);
            tot_ppn = (TextView) itemView.findViewById(R.id.tot_ppn);
            status_faktur = (TextView) itemView.findViewById(R.id.status_faktur);
        }
    }
}

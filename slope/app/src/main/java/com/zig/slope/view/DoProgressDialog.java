package com.zig.slope.view;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.zig.slope.R;
import com.zig.slope.adapter.MySpAdapter;
import com.zig.slope.common.base.bean.ProcessBean;
import com.zig.slope.common.http.cancle.ApiCancleManager;

import java.util.List;
import java.util.Map;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class DoProgressDialog extends Dialog {
    private static View view;
    private Context context;
    private static DoProgressDialog dialog;
    private SeekBar seekBar;
    private TextView text_process_view;
    private Spinner processsp;
    private int selectProcess;

    public DoProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }
    public static DoProgressDialog createDialog(Context context, final boolean canCancle){
        dialog = new DoProgressDialog(context, R.style.CustomProgressDialog);
        view = LayoutInflater.from(context).inflate(R.layout.pops, null);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = 800; // 宽度
        lp.height = 800; // 高度
        dialogWindow.setAttributes(lp);
        dialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (canCancle) {
                        ApiCancleManager.getInstance().cancelAll();
                        return false;
                    }else {
                        return true;
                    }
                }
                return false;
            }
        });
        return dialog;
    }

    public  void  setdata(final List<ProcessBean> data, int process){
        seekBar = view.findViewById(R.id.seekbar);
        text_process_view = view.findViewById(R.id.text_process_view);
        text_process_view.setText("当前治理进度："+process);
        seekBar.setProgress(process);
        seekBar.setEnabled(false);
        processsp = view.findViewById(R.id.processsp);
        processsp.setAdapter(new MySpAdapter(context,data));
        processsp.setSelection(getPosi(data,process));
        selectProcess = process;
        processsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProcessBean bean = data.get(i);
                selectProcess = bean.getId();
                seekBar.setProgress(bean.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public int getSelectProcess() {
        return selectProcess;
    }

    public void setSelectProcess(int selectProcess) {
        this.selectProcess = selectProcess;
    }

    private int getPosi(List<ProcessBean> data, int process){
        for (int i =0;i<data.size();i++){
            if(data.get(i).getId()==process){
                return i;
            }
        }
        return 0;
    }

}

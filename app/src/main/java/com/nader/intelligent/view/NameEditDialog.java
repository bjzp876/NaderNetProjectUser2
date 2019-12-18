package com.nader.intelligent.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nader.intelligent.R;

//import static com.nader.intelligent.view.AddRemoteNameDialog.getInputFilter;


/**
 * Created by 6648 on 2018/6/27.
 * packname: com.shuncom.intelligent.view
 * Descrition:场景名称输入
 */
public class NameEditDialog {
    private Dialog dialog;
    private View dialogView;
    private EditText editor;
    private TextView tv_title;

    public NameEditDialog(Context context) {
        dialog = new Dialog(context, R.style.DialogStyle);
        dialogView = View.inflate(context, R.layout.dialog_for_name_input, null);
        dialog.setContentView(dialogView);
        editor = dialogView.findViewById(R.id.et_name);
//        editor.setFilters(new InputFilter[]{getInputFilter(),new InputFilter.LengthFilter(6)});
        tv_title = dialogView.findViewById(R.id.tv_title);
    }

    /**
     * 设置标题
     *
     * @param value 标题内容
     */
    public void setTitle(String value) {
        tv_title.setText(value);
    }

    public void setCancleListener(View.OnClickListener listener) {
        dialogView.findViewById(R.id.tv_cancle).setOnClickListener(listener);
    }

    public void setSureListener(View.OnClickListener listener) {
        dialogView.findViewById(R.id.tv_sure).setOnClickListener(listener);
    }

    /**
     * 设置编辑框的输入格式
     *
     * @param type InputType
     */
    public void setEditorInputType(int type) {
        editor.setInputType(type);
    }

    /**
     * 设置提示语
     *
     * @param stringRes
     */
    public void setEditorHint(@StringRes int stringRes) {
        editor.setHint(stringRes);
    }

    /*
     * 编辑时展示名称
     * */
    public void setEditorText(String name) {
        editor.setText(name);
//        setEditTextInhibitInputSpace();

//        setEditTextInhibitInputSpace(editor,6);
    }

    public String getText() {
        return editor.getText().toString().trim();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    /**
     * EditText防止输入空格、换行、限制输入字符长度
     * @param editText
     * @param len 长度限制
     */
    public void setEditTextInhibitInputSpace(EditText editText, int len){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if(" ".equals(source) || "\n".equals(source)){
                    //空格和换行都转换为""
                    return "";
                }else{
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(len)});
    }

    public void show() {
        if (dialog != null)
            dialog.show();
    }

    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void setEditTextInhibitInputSpace(){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" ")){
                    return "";
                }else{
                    return null;
                }
            }
        };
        editor.setFilters(new InputFilter[]{filter});
    }


}

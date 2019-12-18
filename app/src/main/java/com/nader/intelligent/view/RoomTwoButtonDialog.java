package com.nader.intelligent.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.nader.intelligent.R;

/**
 * author:zhangpeng
 * date: 2019/11/22
 */
public class RoomTwoButtonDialog extends BaseIndexBotmDialog{
    private TextView page_index_two_button_dialog_message;
    private TextView page_index_two_button_dialog_determine_btn;
    private TextView page_index_two_button_dialog__cancel_btn;
    private String message="你确定要退出吗？";
    private RoomTwoButtonDialoClick roomTwoButtonDialogDetermineClick;
    private RoomTwoButtonDialoClick roomTwoButtonDialogCancelClick;
    private String determinSt="确定";
    private String cancelSt="取消";

    public RoomTwoButtonDialog(@NonNull Context context) {
        super(context);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRoomTwoButtonDialogDetermineClick(RoomTwoButtonDialoClick roomTwoButtonDialogDetermineClick) {
        this.roomTwoButtonDialogDetermineClick = roomTwoButtonDialogDetermineClick;
    }

    public void setRoomTwoButtonDialogCancelClick(RoomTwoButtonDialoClick roomTwoButtonDialogCancelClick) {
        this.roomTwoButtonDialogCancelClick = roomTwoButtonDialogCancelClick;
    }

    public void setDeterminSt(String determinSt) {
        this.determinSt = determinSt;
    }

    public void setCancelSt(String cancelSt) {
        this.cancelSt = cancelSt;
    }

    @Override
    protected void initView() {
        page_index_two_button_dialog_message=findViewById(R.id.page_index_two_button_dialog_message);
        page_index_two_button_dialog_determine_btn=findViewById(R.id.page_index_two_button_dialog_determine_btn);
        page_index_two_button_dialog__cancel_btn=findViewById(R.id.page_index_two_button_dialog__cancel_btn);
        page_index_two_button_dialog_determine_btn.setText(determinSt);
        page_index_two_button_dialog__cancel_btn.setText(cancelSt);
        page_index_two_button_dialog_message.setText(message);
        page_index_two_button_dialog_determine_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roomTwoButtonDialogDetermineClick!=null){
                    roomTwoButtonDialogDetermineClick.onRoomTwoButtonDialoClick(RoomTwoButtonDialog.this);
                }else {
                    dismiss();
                }
            }
        });
        page_index_two_button_dialog__cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roomTwoButtonDialogCancelClick!=null){
                    roomTwoButtonDialogCancelClick.onRoomTwoButtonDialoClick(RoomTwoButtonDialog.this);
                }else {
                    dismiss();
                }
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.page_index_two_button_dialog_layout;
    }

    public  interface RoomTwoButtonDialoClick{
        void onRoomTwoButtonDialoClick(RoomTwoButtonDialog twoButtonDialog);
    }



    public static class Builder{
        private String message;
        private RoomTwoButtonDialoClick roomTwoButtonDialogDetermineClick;
        private RoomTwoButtonDialoClick roomTwoButtonDialogCancelClick;
        private String determinSt;
        private String cancelSt;
        private Context mContext;

        public Builder(Context mContext) {
            this.mContext = mContext;
            determinSt=mContext.getString(R.string.page_index_determine_st);
            cancelSt=mContext.getString(R.string.page_index_cancel_st);
            message=mContext.getString(R.string.page_index_out_message);

        }

        public Builder setMessage(String message){
            this.message=message;
            return this;
        }

        public Builder setRoomTwoButtonDialogDetermineClick(RoomTwoButtonDialoClick roomTwoButtonDialogDetermineClick){
            this.roomTwoButtonDialogDetermineClick=roomTwoButtonDialogDetermineClick;
            return this;
        }

        public Builder setRoomTwoButtonDialogCancelClick(RoomTwoButtonDialoClick roomTwoButtonDialogCancelClick){
            this.roomTwoButtonDialogCancelClick=roomTwoButtonDialogCancelClick;
            return this;
        }

        public Builder setDeterminSt(String determinSt){
            this.determinSt=determinSt;
            return this;
        }

        public Builder setCancelSt(String cancelSt){
            this.cancelSt=cancelSt;
            return this;
        }

        public RoomTwoButtonDialog create(){
            RoomTwoButtonDialog roomTwoButtonDialog=new RoomTwoButtonDialog(mContext);
            roomTwoButtonDialog.setMessage(message);
            roomTwoButtonDialog.setRoomTwoButtonDialogCancelClick(roomTwoButtonDialogCancelClick);
            roomTwoButtonDialog.setRoomTwoButtonDialogDetermineClick(roomTwoButtonDialogDetermineClick);
            roomTwoButtonDialog.setCancelSt(cancelSt);
            roomTwoButtonDialog.setDeterminSt(determinSt);
            return roomTwoButtonDialog;
        }

    }
}

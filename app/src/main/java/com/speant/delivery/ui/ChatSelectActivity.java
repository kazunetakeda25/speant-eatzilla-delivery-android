package com.speant.delivery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.speant.delivery.Common.global.Global;
import com.speant.delivery.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.speant.delivery.Common.global.Global.ADMIN;
import static com.speant.delivery.Common.global.Global.USER;

public class ChatSelectActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_user_chat)
    AppCompatTextView txtUserChat;
    @BindView(R.id.txt_admin_chat)
    AppCompatTextView txtAdminChat;
    private int userId;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_select);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent() != null) {
            Intent intent = getIntent();
            userId = intent.getIntExtra(Global.USER_ID, 0);
            orderId = intent.getStringExtra(Global.ORDER_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.txt_user_chat, R.id.txt_admin_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_user_chat:
                Intent intentChat = new Intent(this, InboxActivity.class);
                if (orderId != null && userId != 0) {
                    intentChat.putExtra(Global.ORDER_ID, orderId);
                    intentChat.putExtra(Global.USER_ID, userId);
                    intentChat.putExtra(Global.FROM_TYPE, USER);
                }
                startActivity(intentChat);
                break;
            case R.id.txt_admin_chat:
                Intent chat = new Intent(this, InboxActivity.class);
                if (orderId != null && userId != 0) {
                    chat.putExtra(Global.ORDER_ID, orderId);
                    chat.putExtra(Global.USER_ID, userId);
                    chat.putExtra(Global.FROM_TYPE, ADMIN);
                }
                startActivity(chat);
                break;
        }
    }
}

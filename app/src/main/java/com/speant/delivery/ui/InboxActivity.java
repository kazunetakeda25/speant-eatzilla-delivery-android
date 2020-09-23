package com.speant.delivery.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.Models.ChatHistory;
import com.speant.delivery.Models.ChatList;
import com.speant.delivery.R;
import com.speant.delivery.ui.Adapter.InboxAdapter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.delivery.Common.SessionManager.KEY_USER_ID;
import static com.speant.delivery.Common.global.Global.USER;


public class InboxActivity extends AppCompatActivity {
    @BindView(R.id.et_send_message)
    AppCompatEditText etSendMessage;
    @BindView(R.id.iv_send)
    AppCompatImageView ivSend;
    @BindView(R.id.bottom_layout)
    RelativeLayout bottomLayout;

    public String TAG = getClass().getSimpleName();

    Animation myAnim;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.iv_back)
    AppCompatImageView ivBack;
    @BindView(R.id.top_lay)
    LinearLayout topLay;

    private WebSocketClient mWebSocketClient;


    ArrayList<ChatList> chatMessageArrayList = new ArrayList<>();
    private InboxAdapter adapter;

    ProgressDialog progressUtils;


    private SessionManager sessionManager;

    int userId;

    String orderId;

    private APIInterface apiInterface;
    private String from_type;
    private int receiverType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        ButterKnife.bind(this);

        progressUtils = new ProgressDialog(this);
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        ivSend.setAnimation(myAnim);


        sessionManager = new SessionManager(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);


        chatMessageArrayList.clear();

        if (getIntent() != null) {
            Intent intent = getIntent();
            from_type = intent.getStringExtra(Global.FROM_TYPE);
            userId = intent.getIntExtra(Global.USER_ID, 0);
            orderId = intent.getStringExtra(Global.ORDER_ID);
            if (from_type.equalsIgnoreCase(USER)) {
                receiverType = 1;
            }else{
                receiverType = 4;
            }
        }

        Log.e(TAG, "onCreate:userId " + userId);
        Log.e(TAG, "onCreate:orderId " + orderId);


        if (orderId != null) {
            callChatApi();
        }


        connectWebSocket();


    }

    private void callChatApi() {

        Call<ChatHistory> call = apiInterface.getChat(sessionManager.getHeader(), orderId, String.valueOf(receiverType));

        call.enqueue(new Callback<ChatHistory>() {
            @Override
            public void onResponse(Call<ChatHistory> call, Response<ChatHistory> response) {

                if (response.isSuccessful()) {

                    Log.e(TAG, "onResponse:ChatResponse " + response);

                    showHistoryChat(response.body());
                    if(response.body().getData().size() > 0 && userId == 0){
                        if (response.body().getData().get(0).getFrom_id().equals(sessionManager.getSocketUniqueId())) {
                            // If the current user is the sender of the message
                           userId = Integer.parseInt(response.body().getData().get(0).getProvider_id());
                        }else if(response.body().getData().get(0).getTo_id().equals(sessionManager.getSocketUniqueId())){
                            userId = Integer.parseInt(response.body().getData().get(0).getUser_id());
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ChatHistory> call, Throwable t) {

            }
        });


    }

    private void sendMessage() {
        JSONObject messageObj = new JSONObject();
        final ChatList chatMessage = new ChatList();


        try {

            messageObj.put("type", "message");
            messageObj.put("message", etSendMessage.getText().toString());
            messageObj.put("from_id", "Provider_" + sessionManager.getUserDetails().get(KEY_USER_ID));



            if (from_type.equalsIgnoreCase(USER)) {
                messageObj.put("to_id", "User_" + userId); // user
                messageObj.put("provider_id", sessionManager.getUserDetails().get(KEY_USER_ID));
                messageObj.put("provider_type", 1);
            } else {
                messageObj.put("to_id", "Admin_" + "1");
                messageObj.put("provider_id", 1);
                messageObj.put("provider_type", 4);
            }

            messageObj.put("user_id", userId);// user
            messageObj.put("request_id", orderId);


            mWebSocketClient.send(String.valueOf(messageObj));

            chatMessage.setFrom_id("Provider_" + sessionManager.getUserDetails().get(KEY_USER_ID));
            chatMessage.setMessage(etSendMessage.getText().toString());
            chatMessage.setProvider_id(String.valueOf(userId));
            chatMessage.setUser_id(sessionManager.getUserDetails().get(KEY_USER_ID));
            chatMessage.setRequest_id(orderId);
            chatMessage.setType("message");
            chatMessage.setTo_id("User_" + userId);// user


            chatMessageArrayList.add(chatMessage);
            showChat(chatMessageArrayList);


            etSendMessage.setText(null);


            Log.e(TAG, "onClick: " + messageObj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void connectWebSocket() {


        Log.e(TAG, "connectWebSocket:is true ");

        String url = Global.SOCKET_URL;

        URI uri;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "connectWebSocket: " + e.toString());
            return;
        }


        mWebSocketClient = new

                WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.i("Websocket", "Opened");

                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("type", "init");
                            jsonObject.put("id", "Provider_" + sessionManager.getUserDetails().get(KEY_USER_ID));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mWebSocketClient.send(String.valueOf(jsonObject));

                        sessionManager.putSocketUinqueId("Provider_" + sessionManager.getUserDetails().get(KEY_USER_ID));

                        Log.e(TAG, "onOpen:getSocketUniqueId() " + sessionManager.getSocketUniqueId());

                    }

                    @Override
                    public void onMessage(String s) {
                        final String message = s;
                        final ChatList chatMessage = new ChatList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Log.e(TAG, "run:getConnection " + mWebSocketClient.getConnection());
                                Log.e(TAG, "run:getReadyState " + mWebSocketClient.getReadyState());


                                Log.e(TAG, "run:Receive  " + message);

                                try {

                                    JSONObject jsonObject = new JSONObject(message);
                                    chatMessage.setFrom_id(jsonObject.optString("from_id"));
                                    chatMessage.setMessage(jsonObject.optString("message"));
                                    chatMessage.setProvider_id(jsonObject.optString("provider_id"));
                                    chatMessage.setUser_id(jsonObject.optString("user_id"));
                                    chatMessage.setRequest_id(jsonObject.optString("request_id"));
                                    chatMessage.setType(jsonObject.optString("type"));
                                    chatMessage.setTo_id(jsonObject.optString("to_id"));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                chatMessageArrayList.add(chatMessage);
                                Log.e(TAG, "run:CheckUser " + chatMessageArrayList);
                                Log.e(TAG, "sendMessage:afterSend " + chatMessageArrayList.size());

                                showChat(chatMessageArrayList);


                            }
                        });
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.i("Websocket", "Closed " + s);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Websocket", "Error " + e.getMessage());
                    }
                };


        mWebSocketClient.connect();


    }

    @Override
    protected void onResume() {
        super.onResume();
        connectWebSocket();
    }

    @SuppressLint("WrongConstant")
    private void showChat(ArrayList<ChatList> chatMessageArrayList) {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);
        adapter = new InboxAdapter(this, chatMessageArrayList);
        rvChat.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    public void showHistoryChat(ChatHistory details) {


        if (details.getData().size() > 0) {


            for (int i = 0; i < details.getData().size(); i++) {
                ChatList chatList = new ChatList();

                chatList.setFrom_id(details.getData().get(i).getFrom_id());
                chatList.setMessage(details.getData().get(i).getMessage());
                chatList.setProvider_id(details.getData().get(i).getProvider_id());
                chatList.setUser_id(details.getData().get(i).getUser_id());
                chatList.setRequest_id(details.getData().get(i).getRequest_id());
                chatList.setType(details.getData().get(i).getType());
                chatList.setTo_id(details.getData().get(i).getTo_id());

                chatMessageArrayList.add(chatList);


            }

            Log.e(TAG, "showHistoryChat: " + chatMessageArrayList.size());

            showChat(chatMessageArrayList);
        }


    }

    @OnClick({R.id.iv_back, R.id.iv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_send:


                if (etSendMessage.getText().toString().length() > 0 && etSendMessage.getText().toString().trim().matches("")) {

                    Toast.makeText(this, "Empty spaces are not allowed!!!", Toast.LENGTH_SHORT).show();
                    etSendMessage.setText(null);


                } else if (etSendMessage.getText().toString().isEmpty()) {

                    Toast.makeText(this, "Please Type Message Here!!!", Toast.LENGTH_SHORT).show();


                } else {

                    view.startAnimation(myAnim);


                    if (mWebSocketClient.getReadyState().toString().equals("OPEN")) {

                        if (userId != 0 && orderId != null) {

                            sendMessage();
                        }
                    } else {

                        Toast.makeText(this, "Connection Not Established!!!", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }
}

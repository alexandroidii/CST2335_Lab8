package com.example.alexr.cst2335_lab3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity implements AdapterView.OnItemClickListener {
    protected static final String ACTIVITY_NAME = "1234 ChatWindow";
    protected static final int MESSAGE_DETAILS_RESPONSE = 1;

    ChatDatabaseHelper chatDatabaseHelper;

    ListView chatListView;
    EditText chatMessageEditText;
    Button chatSendBtn;
    Cursor cursor;
    boolean isTablet = false;

    ArrayList<String> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tabletMessageDetailFrame);
        if (frameLayout == null) {
            Log.i(ACTIVITY_NAME, "Fragment in Phone layout.");
            isTablet = false;
        } else {
            Log.i(ACTIVITY_NAME, "Fragment in Tablet layout.");
            isTablet = true;

        }

        chatDatabaseHelper = new ChatDatabaseHelper(this);
        /* Source: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
         *  Author: Inzimam Tariq IT
         *  Date: 2016-05-12
         *  This enables the back button on the action bar to return to the start activity.
         */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        chatListView = (ListView) findViewById(R.id.chatListView);
        chatMessageEditText = (EditText) findViewById(R.id.chatMessageEditText);
        chatSendBtn = (Button) findViewById(R.id.chatSendBtn);

        SQLiteDatabase db = chatDatabaseHelper.getWritableDatabase();

        cursor = db.rawQuery("select * from " + chatDatabaseHelper.TABLE_NAME, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int columnIndex = cursor.getColumnIndex(chatDatabaseHelper.KEY_MESSAGE);
            String message = cursor.getString(columnIndex);
            chatMessages.add(message);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
            cursor.moveToNext();
        }

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column [" + i + "] : " + cursor.getColumnName(i));
        }


        //in this case, "this" is the ChatWindow, which is-A Context object
        ChatAdapter messageAdapter = new ChatAdapter(this);
        chatListView.setAdapter(messageAdapter);


        chatSendBtn.setOnClickListener(
                (View v) -> {
                    chatMessages.add(chatMessageEditText.getText().toString());

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(chatDatabaseHelper.KEY_MESSAGE, chatMessageEditText.getText().toString());
                    long newRowId = db.insert(chatDatabaseHelper.TABLE_NAME, null, contentValues);

                    if (newRowId == -1) {
                        Log.i(ACTIVITY_NAME, "Chat message failed to write to database");
                    }
                    messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                    chatMessageEditText.setText("");
                });


        /*
         * Source: https://stackoverflow.com/questions/4709870/setonitemclicklistener-on-custom-listview
         * Author: Charu
         * Date: 2017-01-17
         *
         * Also used onitemclicklistener-with-custom-adapter-and-listview on stackoverflow.
         * Author: coading fever
         * date: 2012-06-29
         */
        chatListView.setOnItemClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == MESSAGE_DETAILS_RESPONSE && responseCode == RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        } else {
            super.onActivityResult(requestCode, responseCode, data);
        }
    }

    /*
    source: https://www.simplifiedcoding.net/bottom-navigation-android-example/
    author: Belal Khan
    Date: 2018-01-23
    */
    private boolean loadFragment(Fragment fragment, int frameId, Bundle bundle) {
        //switching fragment

        if(bundle != null){
            fragment.setArguments(bundle);
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(frameId, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /* Source: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
     *  Author: Inzimam Tariq IT
     *  Date: 2016-05-12
     *  This enables the back button on the action bar to return to the start activity.
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            Intent messageDetailsIntent = new Intent(ChatWindow.this, MessageDetails.class );
            messageDetailsIntent.putExtra("messageId", id );
            messageDetailsIntent.putExtra("messageText", "test" ); // get the right message

        if (isTablet) {

            loadFragment(new MessageFragment(), R.id.tabletMessageDetailFrame, messageDetailsIntent.getExtras());
        } else {

            startActivityForResult(messageDetailsIntent, MESSAGE_DETAILS_RESPONSE);
        }
    }

    public class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return chatMessages.size();
        }

        @Override
        public String getItem(int position) {
            return chatMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);

            int columnIndex = cursor.getColumnIndex(chatDatabaseHelper.KEY_ID);
            Long id = cursor.getLong(columnIndex);

            return id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;

        }


    }

    @Override
    public void onDestroy() {
        chatDatabaseHelper.close();
        super.onDestroy();
    }
}

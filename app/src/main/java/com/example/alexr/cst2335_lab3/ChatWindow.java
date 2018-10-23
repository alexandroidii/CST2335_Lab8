package com.example.alexr.cst2335_lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "1234 ChatWindow";

    ChatDatabaseHelper chatDatabaseHelper;

    ListView chatListView;
    EditText chatMessageEditText;
    Button chatSendBtn;

    ArrayList<String> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
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

        Cursor cursor = db.rawQuery("select * from " + chatDatabaseHelper.TABLE_NAME, null);

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

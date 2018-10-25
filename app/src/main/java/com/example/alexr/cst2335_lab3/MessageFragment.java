package com.example.alexr.cst2335_lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    private static final String ACTIVITY_NAME = "1234 MessageFragment";

    FragmentActivity listener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragement_message_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragement 1", "onActivityCreated");

        Intent intent = getActivity().getIntent();
        Bundle bundle = getArguments();
        intent.putExtras(bundle);

        Long id = bundle.getLong("messageId");
        String messageDetails = bundle.getString("message");

        TextView messageIdTextView = (TextView)getView().findViewById(R.id.messageDetailIdTextView);
        messageIdTextView.setText(id.toString());

        TextView messageDetailsTextView = (TextView)getView().findViewById(R.id.messageDetailTextView);
        messageDetailsTextView.setText(messageDetails);

        Button deleteMessageBtn = (Button)getView().findViewById(R.id.deleteMessageBtn);

        deleteMessageBtn.setOnClickListener(
                (View v) ->{
                    Log.i(ACTIVITY_NAME, "Deleting item #" + bundle.getLong("id"));
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }

        );

    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }
}

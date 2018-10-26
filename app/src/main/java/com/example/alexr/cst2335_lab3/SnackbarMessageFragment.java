package com.example.alexr.cst2335_lab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SnackbarMessageFragment extends DialogFragment {

    public String editTextValue;
    private EditText editTextForSnackbar;

    public EditText getEditTextForSnackbar(){
        return editTextForSnackbar;
    }

    public void setEditTextForSnackbar(EditText editText){
        this.editTextForSnackbar = editText;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.snackbar_dialog_fragment, null);
        builder.setView(view);

        setEditTextForSnackbar((EditText)view.findViewById(R.id.snackbarDialogEditText));

        builder.setTitle("What can i feed you?")
                .setPositiveButton("Feed Me!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(getActivity().findViewById(R.id.testToolbarview), getEditTextForSnackbar().getText(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();                    }
                })
                .setNegativeButton("Sorry I'm full", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }
}

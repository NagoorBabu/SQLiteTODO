package com.example.nagoor.sqlitetodo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nagoor.sqlitetodo.database.DatabaseHelper;
import com.example.nagoor.sqlitetodo.modelclass.Task;

/**
 * Created by nagoor on 04/08/17.
 */

public class CreateTaskDialog extends DialogFragment {


    public CreateTaskDialog() {

    }

    public interface DialogListener{

        public void onSuccess(String title, String description);

    }

    private DialogListener dialogListener;

    public void setListener(DialogListener dialogListener){

        this.dialogListener = dialogListener;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getting proper access to LayoutInflater is the trick. getLayoutInflater is a                   //Function
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_createtask, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        TextView header = (TextView) view.findViewById(R.id.header);
        final EditText title = (EditText) view.findViewById(R.id.title);
        final EditText description = (EditText) view.findViewById(R.id.description);
        Button ok = (Button) view.findViewById(R.id.btn);

        Bundle bundle = getArguments();

        if (bundle.getBoolean("createtask", true)){

            header.setText("Create Task");

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (title.getText().toString().trim().isEmpty()){

                        Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_LONG).show();
                    }else if (description.getText().toString().trim().isEmpty()){

                        Toast.makeText(getActivity(), "Enter Description", Toast.LENGTH_LONG).show();
                    }else {

                        if (dialogListener!=null)
                            dialogListener.onSuccess(title.getText().toString(), description.getText().toString());

                        dismiss();
                    }
                }
            });

        }else {

            header.setText("Edit Task");
            title.setText(bundle.getString("title", ""));
            description.setText(bundle.getString("description", ""));

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (title.getText().toString().trim().isEmpty()){

                        Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_LONG).show();
                    }else if (description.getText().toString().trim().isEmpty()){

                        Toast.makeText(getActivity(), "Enter Description", Toast.LENGTH_LONG).show();
                    }else {

                        if (dialogListener!=null)
                            dialogListener.onSuccess(title.getText().toString(), description.getText().toString());

                        dismiss();

                    }


                }
            });
        }

        return builder.create();
    }



}
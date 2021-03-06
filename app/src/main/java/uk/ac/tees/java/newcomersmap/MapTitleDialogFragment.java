package uk.ac.tees.java.newcomersmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MapTitleDialogFragment extends DialogFragment {

    private EditText editTextMapTitle;
    private DialogListener dialogListener;
    private UserMap mUserMap;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_map_title, null);

        editTextMapTitle = view.findViewById(R.id.edit_map_title);
        editTextMapTitle.setText(mUserMap.getTitle());
        editTextMapTitle.selectAll();
        builder.setView(view)
                .setTitle("Map title:")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogListener != null) {
                            dialogListener
                                    .onDialogResult(DialogResult.CANCEL_PRESSED);
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogListener != null) {
                            String title = editTextMapTitle.getText().toString().trim();
                            if (title.isEmpty() || title.length() < 3 || title.length() > 16) {
                                dialogListener.onDialogResult(DialogResult.INPUT_INVALID);
                            } else {
                                mUserMap.setTitle(title);
                                dialogListener.onDialogResult(DialogResult.INPUT_OK);
                            }
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Show keyboard when fragments get created
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setNewcomerMap(UserMap mUserMap) {
        this.mUserMap = mUserMap;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

}

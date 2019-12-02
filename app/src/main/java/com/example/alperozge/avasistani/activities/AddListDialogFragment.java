package com.example.alperozge.avasistani.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.alperozge.avasistani.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditableItemsActivity} interface
 * to handle interaction events.
 * Use the {@link AddListDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddListDialogFragment extends AppCompatDialogFragment {
    public enum OperationMode { EDIT , ADD}

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EDITTEXT_TEXT = "param1";
    private static final String ARG_SELECTION_TEXT_START = "param2";
    private static final String ARG_SELECTION_TEXT_END = "param3" ;
    private static final String ARG_OPTIONAL_TAG = "tag";
    private static final String ARG_OPERATION_MODE = "mode";

    private OperationMode mOperationMode;


    private TextInputEditText mViewEditText;
    private EditableItemsActivity mEditableItemsActivity;

    public AddListDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment AddListDialogFragment.
     */

    public static AddListDialogFragment newInstance(@Nullable String param1 , @NotNull OperationMode operationMode , @Nullable Parcelable tag) {
        AddListDialogFragment fragment = new AddListDialogFragment();

        Bundle args = new Bundle();
        if (null != param1) {
            args.putString(ARG_EDITTEXT_TEXT, param1);
        }
        if(null == operationMode) {
            throw new IllegalArgumentException("OperationMode parameter cannot be null");
        }
        args.putInt(ARG_OPERATION_MODE,operationMode == OperationMode.ADD ? 0 : 1);


        if(null != tag) {
            args.putParcelable(ARG_OPTIONAL_TAG , tag);
        }
//        args.putString(ARG_SELECTION_TEXT_START, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddListDialogFragment newInstance(@Nullable String param1 , OperationMode operationMode) {
        return newInstance(param1,operationMode,null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mOperationMode = getArguments().getInt(ARG_OPERATION_MODE) == 0 ? OperationMode.ADD : OperationMode.EDIT;
////            mEditableItemText = getArguments().getString(ARG_EDITTEXT_TEXT);
//////            mCaretSelectionStart = getArguments().getInt(ARG_SELECTION_TEXT_START);
//////            mCareSelectionEnd = getArguments().getInt(ARG_SELECTION_TEXT_END);
//////            mParam2 = getArguments().getString(ARG_SELECTION_TEXT_START);
//        } else if (null != savedInstanceState){
////            mEditableItemText = savedInstanceState.getString(ARG_EDITTEXT_TEXT);
////            mCaretSelectionStart = savedInstanceState.getInt(ARG_SELECTION_TEXT_START);
////            mCareSelectionEnd = savedInstanceState.getInt(ARG_SELECTION_TEXT_END);
//        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflatedView = inflater.inflate(R.layout.fragment_add_list_dialog , null);


        final TextInputLayout textEdit = inflatedView.findViewById(R.id.list_name_text_input);
        mViewEditText = (TextInputEditText) textEdit.getEditText();
        if (getArguments() != null) {
            mViewEditText.setText(getArguments().getString(ARG_EDITTEXT_TEXT));
            mViewEditText.setSelection(getArguments().getInt(ARG_SELECTION_TEXT_START) , getArguments().getInt(ARG_SELECTION_TEXT_END));
        }

        // Add action buttons
        builder.setView(inflatedView)
                .setPositiveButton(getString(R.string.action_add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mOperationMode == OperationMode.ADD) {
                            mEditableItemsActivity.onAddReturnPositive(textEdit.getEditText().getText().toString().trim(),getArguments().getParcelable(ARG_OPTIONAL_TAG));
                        } else {
                            mEditableItemsActivity.onEditReturnPositive(textEdit.getEditText().getText().toString().trim(),getArguments().getParcelable(ARG_OPTIONAL_TAG));
                        }
                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mEditableItemsActivity.onReturnNegative();
                    }
                });
        return builder.create();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_list_dialog, container, false);
//    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(context instanceof EditableItemsActivity)) {
            throw new ClassCastException("Attached activity should implement EditableItemsActivity");
        } else {
            mEditableItemsActivity = (EditableItemsActivity) context;
        }

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(null != getArguments())
        getArguments().putString(ARG_EDITTEXT_TEXT, mViewEditText.getText().toString());
        getArguments().putInt(ARG_SELECTION_TEXT_START, mViewEditText.getSelectionStart());
        getArguments().putInt(ARG_SELECTION_TEXT_END , mViewEditText.getSelectionEnd());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mOperationMode = getArguments().getInt(ARG_OPERATION_MODE) == 0 ? OperationMode.ADD : OperationMode.EDIT;
        }
    }
}

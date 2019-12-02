package com.example.alperozge.avasistani.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.text.HtmlCompat;


import com.example.alperozge.avasistani.R;
import com.example.alperozge.avasistani.util.SpanFormatter;

import java.util.ArrayList;
import java.util.List;


public class SimpleAlertFragment extends AppCompatDialogFragment {

    interface KeyBuilder extends Parcelable{
        String getNextKey();
        String getKeyForInt(int i);
        List<String> getAllBuiltKeys();
    }

    private static final String TITLE = "title";
    private static final String HIGHLIGHTED_INPUTS = "input";
    private static final String HTML_MESSAGE = "message";
    private static final String KEY_BUILDER = "keyBuilder";

    private SimpleAlertDisplayerActivity mActivity;
    private TextView mTextView;
//    private KeyBuilder mKeyBuilder;
    public SimpleAlertFragment() {
    }


    /**
     * Simple alert fragment fabrication method. This method should be used instead of directly using the no-arg constructor in order
     * to get and show a Alert Fragment.
     *
     * @param title            Title to be shown.
     * @param formatMessage    Information Message to be displayed inside dialog window. It might contain a single format argument
     * @param highlightedInputs If a formatted String is provided as {@code formatMessage}, then this argument is placed in. This is
     *                         implemented as a {@code CharSequence} rather than a {@code String} so a {@code SpannedString} can
     *                         be passed in and it will be displayed as desired.
     */
    public static SimpleAlertFragment newInstance(@Nullable String title, String formatMessage, @Nullable CharSequence... highlightedInputs) {


        SimpleAlertFragment fragment = new SimpleAlertFragment();


        Bundle args = new Bundle();

        if (null != title) {
            args.putString(TITLE, title);
        }

        SpannedString spanned = SpanFormatter.format(formatMessage,highlightedInputs);
//        SpannableString spanned = new SpannableString("AAA BBB");
//        spanned.setSpan(new AbsoluteSizeSpan(36,true),4,7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spanned.setSpan(new ForegroundColorSpan(146),4,7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        args.putString(HTML_MESSAGE, HtmlCompat.toHtml(spanned,HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE));
        args.putCharSequenceArray(HIGHLIGHTED_INPUTS , highlightedInputs);
//        if (null != highlightedInputs) {
//            KeyBuilder keyBuilder = new HighlightedInputKeyBuilder(HIGHLIGHTED_INPUT);
//            for(CharSequence cs : highlightedInputs) {
//                args.putCharSequence(keyBuilder.getNextKey(),cs);
//            }
//            args.putParcelable(KEY_BUILDER , keyBuilder);
//        }


        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (!(context instanceof SimpleAlertDisplayerActivity)) {
            throw new ClassCastException("Activity should implement SimpleAlertDisplayerActivity");
        }
        super.onAttach(context);
        mActivity = (SimpleAlertDisplayerActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_simple_alert, null, false);
        mTextView = view.findViewById(R.id.alert_text_view);
        Bundle args = getArguments();
        if (null != args) {
            // Set title - if fragment arguments is not null, get it from there
            builder.setTitle(args.getString(TITLE));

            String htmlContent = args.getString(HTML_MESSAGE);

            Log.d("SimpleAlertFragment", "onCreateDialog: Spanned -->\n" + htmlContent );
            mTextView.setText(HtmlCompat.fromHtml( htmlContent,HtmlCompat.FROM_HTML_MODE_COMPACT));

//            SpannableString spannableString = new SpannableString("AAA AAD");
//            spannableString.setSpan(new AbsoluteSizeSpan(24,true),4,7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            mTextView.setText(spannableString);
        }
//        String higligtedInput = null != getArguments() ? getArguments().getString(HIGHLIGHTED_INPUT, "") : "";
//        String contentText1 = getString(R.string.already_present_tobuylist, higligtedInput);

//        int highlightStart = contentText1.lastIndexOf(higligtedInput);
//        int highlightEnd = highlightStart + higligtedInput.length();
//
//        contentText1 = contentText1.concat(getString(R.string.do_you_want_to_retry));


//        KeyBuilder keyBuilder = args.getParcelable(KEY_BUILDER);
//        List<String> higlightedInputs = null;
//        if(null != keyBuilder) {
//            List<String> highlightedInputKeys = keyBuilder.getAllBuiltKeys();
//            for(String key : highlightedInputKeys) {
//                higlightedInputs.add(args.)
//            }
//        }
//        SpannedString spannableString = new SpannableString(contentText1);
//
//        SpannableStringBuilder builder1 = new SpannableStringBuilder();
////        builder1.
//        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorAccent))
//                , highlightStart, highlightEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        if () {
//
//        }
        builder.setView(view)
                .setPositiveButton(getString(R.string.yes), (diaInt, i) -> mActivity.onAlertReturnPositive(getArguments().getCharSequenceArray(HIGHLIGHTED_INPUTS)))
                .setNegativeButton(getString(R.string.no), (dia, i) -> mActivity.onAlertReturnNegative());

        return builder.create();
    }


    public static class HighlightedInputKeyBuilder implements KeyBuilder  {

        private String mKeyBase;
        private int mCount = 0;

        public static final String SEPERATOR = "_";

        HighlightedInputKeyBuilder(String keyBase) {
            mKeyBase = keyBase;
        }

        public HighlightedInputKeyBuilder(Parcel in) {
            mKeyBase = in.readString();
            mCount = in.readInt();
        }

        /* KeyBuilder interface implementations
        *  */
        @Override
        public String getNextKey() {
            return mKeyBase.concat(SEPERATOR).concat(Integer.toString(mCount++));

        }

        @Override
        public String getKeyForInt(int i) {
            return mKeyBase.concat(SEPERATOR).concat(Integer.toString(i));
        }

        @Override
        public List<String> getAllBuiltKeys() {
            List<String> allKeys = new ArrayList<>(mCount);
            for(int i = 0; i < mCount; ++i) {
                allKeys.add(mKeyBase.concat(SEPERATOR).concat(Integer.toString(i)));
            }
            return allKeys;
        }

        /*
        * Parcelable implementation
        * */
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public HighlightedInputKeyBuilder createFromParcel(Parcel in) {
                return new HighlightedInputKeyBuilder(in);
            }

            public HighlightedInputKeyBuilder[] newArray(int size) {
                return new HighlightedInputKeyBuilder[size];
            }
        };


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mKeyBase);
            parcel.writeInt(mCount);
        }
    }



}

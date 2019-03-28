package com.mcho.recipient.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mcho.recipient.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentRequisition extends BottomSheetDialogFragment {

    @BindView(R.id.stockDescription) TextView stockdescription;

    public FragmentRequisition() {
        // Required empty public constructor
    }

    public static FragmentRequisition getInstance() {
        return new FragmentRequisition();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requisition, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        stockdescription.setText(bundle.getString("stock_desc"));
    }
}

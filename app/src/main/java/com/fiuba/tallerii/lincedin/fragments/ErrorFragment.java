package com.fiuba.tallerii.lincedin.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tallerii.lincedin.R;


public class ErrorFragment extends Fragment {

    public interface OnErrorFragmentInteractionListener {
        void onRetry();
    }

    private OnErrorFragmentInteractionListener mListener;

    public ErrorFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_error, container, false);
        setRetryListener(convertView);
        return convertView;
    }

    private void setRetryListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnErrorFragmentInteractionListener) {
            mListener = (OnErrorFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

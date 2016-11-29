package com.fiuba.tallerii.lincedin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tallerii.lincedin.R;

public class UserNotLoggedFragment extends Fragment {

    public interface OnUserNotLoggedFragmentInteractionListener {
        void onLoginButtonTapped();
    }

    private OnUserNotLoggedFragmentInteractionListener mListener;

    public UserNotLoggedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_not_logged, container, false);
        setLoginButtonListener(fragmentView);
        return fragmentView;
    }

    private void setLoginButtonListener(View view) {
        view.findViewById(R.id.fragment_user_not_logged_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLoginButtonTapped();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserNotLoggedFragmentInteractionListener) {
            mListener = (OnUserNotLoggedFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserNotLoggedFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

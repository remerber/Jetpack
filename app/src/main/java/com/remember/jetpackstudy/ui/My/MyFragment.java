package com.remember.jetpackstudy.ui.My;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.remember.jetpackstudy.R;
import com.remember.libnavannotation.FragmentDestination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@FragmentDestination(pageUrl = "main/tabs/my", asStarter = false)
public class MyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = new TextView(getContext());
        textView.setText("this is MyFragment");
        return root;
    }
}
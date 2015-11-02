package com.example.xmeng.imagesearch.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.xmeng.imagesearch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterDialogFragment extends DialogFragment {

    Spinner spinnerColor;
    Spinner spinnerType;
    Spinner spinnerSize;
    EditText etSite;
    String[] COLOR = {"", "black", "blue", "brown", "gray", "green", "orange", "pink", "purple", "red"};
    String[] SIZE = {"", "small", "medium", "large", "xlarge"};
    String[] TYPE = {"", "face", "photo", "clipart", "lineart"};
    public interface FilterDialogListener {
        public void onFinishEditing(String size, String color, String type, String site);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        getDialog().setTitle("Edit Filter");
        spinnerColor = (Spinner) view.findViewById(R.id.spinnerColor);
        spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
        spinnerSize = (Spinner) view.findViewById(R.id.spinnerSize);

        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, COLOR);
        ArrayAdapter<String> tAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, TYPE);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SIZE);
        spinnerColor.setAdapter(cAdapter);
        spinnerSize.setAdapter(sAdapter);
        spinnerType.setAdapter(tAdapter);
        etSite = (EditText) view.findViewById(R.id.etSite);

        Button btCancel = (Button)view.findViewById(R.id.btCancel);
        Button btSave = (Button)view.findViewById(R.id.btSave);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = spinnerSize.getSelectedItem().toString();
                String color = spinnerColor.getSelectedItem().toString();
                String type = spinnerType.getSelectedItem().toString();
                String site = etSite.getText().toString();
                FilterDialogListener activity = (FilterDialogListener) getActivity();
                activity.onFinishEditing(size, color, type, site);
                getDialog().dismiss();
            }
        });
        return view;
    }


}

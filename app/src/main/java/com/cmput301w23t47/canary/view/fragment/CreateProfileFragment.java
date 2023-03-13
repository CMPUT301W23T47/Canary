package com.cmput301w23t47.canary.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.databinding.FragmentCreateProfileBinding;
import com.cmput301w23t47.canary.model.Player;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateProfileFragment extends Fragment implements OperationStatusCallback {

    private FragmentCreateProfileBinding binding;
    private FirestorePlayerController firestorePlayerController;

    public CreateProfileFragment() {
        // Required empty public constructor
    }

    public static CreateProfileFragment newInstance() {
        CreateProfileFragment fragment = new CreateProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateProfileBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }

    public boolean isEmptyEditText(EditText e){
        return e.getText().toString().trim().length() == 0;
    }

    private boolean allValuesFilled(){
        return (!isEmptyEditText(binding.usernameEditView)) &&
                (!isEmptyEditText(binding.firstNameEditView)) &&
                (!isEmptyEditText(binding.lastNameEditView)) &&
                (!isEmptyEditText(binding.contactEditView));
    }

    private void init(){
        firestorePlayerController = new FirestorePlayerController();
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allValuesFilled()){
                    String username = binding.usernameEditView.getText().toString();
                    String firstName = binding.firstNameEditView.getText().toString();
                    String lastName = binding.lastNameEditView.getText().toString();
                    Player player = new Player(username, firstName, lastName);
                    firestorePlayerController.createPlayer(player, CreateProfileFragment.this);
                }
                else{
                    CharSequence fillAllFields = "Please enter all Fields to continue!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), fillAllFields,duration );
                    toast.show();
                }
            }
        });
    }

    /**
     * Navigate to home fragment
     */
    private void navigateToHome() {
        Navigation.findNavController(getView()).navigate(R.id.action_createProfileToHome);
    }

    @Override
    public void operationStatus(boolean status) {
        if (status) {
            navigateToHome();
        } else {
            CharSequence errorMessage = "Couldn't create player!";
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
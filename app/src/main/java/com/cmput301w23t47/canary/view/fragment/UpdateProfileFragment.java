package com.cmput301w23t47.canary.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.databinding.FragmentUpdateProfileBinding;
import com.cmput301w23t47.canary.model.Player;

/**
 * Fragment for updating Player Profile
 */
public class UpdateProfileFragment extends Fragment implements OperationStatusCallback, GetPlayerCallback {
    FragmentUpdateProfileBinding binding;
    private FirestorePlayerController firestorePlayerController;
    private Player player;

    public UpdateProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    /**
     * Initializes the UI for this page.
     */
    private void init(){
        firestorePlayerController = new FirestorePlayerController();
        firestorePlayerController.getCurrentPlayerShallow(this);

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allValuesFilled()){
                    String username = binding.usernameEditView.getText().toString();
                    String firstName = binding.firstNameEditView.getText().toString();
                    String lastName = binding.lastNameEditView.getText().toString();
                    String contactInfo = binding.contactEditView.getText().toString();
                    Player player = new Player(username, firstName, lastName, contactInfo);
                    firestorePlayerController.updatePlayerProfile(player, UpdateProfileFragment.this);
                    showLoadingBar();
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
     * Checks if the editText in empty or not
     * @param e An editText object from the layout
     * @return boolean value for empty or not
     */
    public boolean isEmptyEditText(EditText e){
        return e.getText().toString().trim().length() == 0;
    }

    /**
     * Checks if all the editText boxes are filled or not
     * @return boolean value for empty or not
     */
    private boolean allValuesFilled(){
        return (!isEmptyEditText(binding.usernameEditView)) &&
                (!isEmptyEditText(binding.firstNameEditView)) &&
                (!isEmptyEditText(binding.lastNameEditView)) &&
                (!isEmptyEditText(binding.contactEditView));
    }

    /**
     * Navigate to home fragment
     */
    private void navigateBack() {
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }

    /**
     * Depending upon status, determines whether the player has been created or not.
     * @param status boolean value giving if a state is valid or not
     */
    @Override
    public void operationStatus(boolean status) {
        hideLoadingBar();
        if (status) {
            navigateBack();
        } else {
            CharSequence errorMessage = "Couldn't edit player!";
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getPlayer(Player player) {
        this.player = player;
        updateFields();
    }

    private void updateFields() {
        if (player == null) {
            return;
        }
        binding.usernameEditView.setText(player.getUsername());
        binding.firstNameEditView.setText(player.getFirstName());
        binding.lastNameEditView.setText(player.getLastName());
        binding.contactEditView.setText(player.getContactInfo());
        hideLoadingBar();
    }

    /**
     * Hides the loading bar
     */
    private void hideLoadingBar() {
        binding.progressBarBox.setVisibility(View.GONE);
    }

    /**
     * Shows the loading bar
     */
    private void showLoadingBar() {
        binding.progressBarBox.setVisibility(View.VISIBLE);
    }
}
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
 * Fragment for Creating the profile for the player
 */
public class CreateProfileFragment extends Fragment implements OperationStatusCallback {

    private FragmentCreateProfileBinding binding;
    private FirestorePlayerController firestorePlayerController;

    public CreateProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new CreateProfileFragment and returns it
     * @return fragment (CreateProfileFragment): a new fragment created to work on
     */
    public static CreateProfileFragment newInstance() {
        CreateProfileFragment fragment = new CreateProfileFragment();
        return fragment;
    }

    /**
     * Handles the layout of the activity, and called on activity creation.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Initializes the view and creates a bundle object for this view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the binding object on which we can work
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateProfileBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
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
     * Initializes the UI for this page.
     */
    private void init(){
        firestorePlayerController = new FirestorePlayerController();
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allValuesFilled()){
                    String username = binding.usernameEditView.getText().toString();
                    String firstName = binding.firstNameEditView.getText().toString();
                    String lastName = binding.lastNameEditView.getText().toString();
                    String contactInfo = binding.contactEditView.getText().toString();
                    Player player = new Player(username, firstName, lastName, contactInfo);
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
        Navigation.findNavController(binding.getRoot()).navigate(CreateProfileFragmentDirections.actionCreateProfileFragmentToHomeActivity());
    }

    /**
     * Depending upon status, determines whether the player has been created or not.
     * @param status boolean value giving if a state is valid or not
     */
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
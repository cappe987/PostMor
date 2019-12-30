package com.mdhgroup2.postmor.Contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.UserCard;


public class ContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Contact friend = null;

    // Below are all that exist in the add popup dialog view.
    private View popupAddInputDialogView = null;
    private EditText userSearch = null;
    private Button searchUserbutton = null;
    private Button cancelUserAddButton = null;

    // Below are all that exist in the found popup dialog view.
    private View popupFoundUserView = null;
    private TextView userFoundName = null;
    private TextView userFoundAddress = null;
    private Button addUserbutton = null;
    private Button cancelUserFoundButton = null;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);

        recyclerView = view.findViewById(R.id.contactsRecyclerView);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final MainActivityViewModel mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        // specify an adapter (see also next example)
        mAdapter = new ContactsAdapter(mViewModel.getContactList());
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton addContact = view.findViewById(R.id.floatingAddButton);
        addContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Search for a user by typing in their address");
                alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                alertDialogBuilder.setCancelable(true);

                // Init popup dialog view and it's ui controls.
                initAddPopupViewControls();

                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(popupAddInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                // When user click the save user data button in the popup dialog.
                searchUserbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userAddress = userSearch.getText().toString();
                        if(userAddress.isEmpty()){
                            Toast toast = Toast.makeText(getContext(), "You need to enter an address.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        if (!userAddress.isEmpty()){
                            friend = mViewModel.findUserByAddress(userAddress);
                            if (friend != null && !friend.IsFriend) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                alertDialogBuilder.setTitle("User found");
                                alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                                alertDialogBuilder.setCancelable(true);

                                // Init popup dialog view and it's ui controls.
                                initFoundPopupViewControls(friend);

                                // Set the inflated layout view object to the AlertDialog builder.
                                alertDialogBuilder.setView(popupFoundUserView);

                                // Create AlertDialog and show.
                                final AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                                addUserbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mViewModel.addUserToContacts(friend);
                                        mAdapter.notifyDataSetChanged();
                                        Toast toast = Toast.makeText(getContext(), friend.Name + " has been added to your contacts.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        alertDialog.cancel();
                                    }
                                });
                                cancelUserFoundButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.cancel();
                                    }
                                });
                            }
                            else if(friend != null && friend.IsFriend){
                                Toast toast = Toast.makeText(getContext(), friend.Name + " is already in your contacts.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else{
                                Toast toast = Toast.makeText(getContext(), "That user could not be found.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                    }
                });

                cancelUserAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initAddPopupViewControls()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getParentFragment().getContext());

        // Inflate the popup dialog from a layout xml file.
        popupAddInputDialogView = layoutInflater.inflate(R.layout.add_contact_popup, null);

        // Get user input edittext and button ui controls in the popup dialog.
        userSearch = (EditText) popupAddInputDialogView.findViewById(R.id.userName);
        searchUserbutton = popupAddInputDialogView.findViewById(R.id.button_search_user);
        cancelUserAddButton = popupAddInputDialogView.findViewById(R.id.button_cancel_user_add);
    }

    private void initFoundPopupViewControls(UserCard uc){
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getParentFragment().getContext());

        // Inflate the popup dialog from a layout xml file.
        popupFoundUserView = layoutInflater.inflate(R.layout.found_contact_popup, null);

        // Get user input edittext and button ui controls in the popup dialog.
        userFoundName = popupFoundUserView.findViewById(R.id.found_user_name);
        userFoundName.setText(uc.Name);
        userFoundAddress = popupFoundUserView.findViewById(R.id.found_user_address);
        userFoundAddress.setText(uc.Address);
        addUserbutton = popupFoundUserView.findViewById(R.id.button_add_user);
        cancelUserFoundButton = popupFoundUserView.findViewById(R.id.button_cancel_user_found);
    }

}

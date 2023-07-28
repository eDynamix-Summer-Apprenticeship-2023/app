package com.example.edynamixapprenticeship.ui.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.databinding.AudioFragmentBinding;
import com.example.edynamixapprenticeship.databinding.FragmentChatBinding;
import com.example.edynamixapprenticeship.model.chat.Message;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.User;


public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private final MutableLiveData<List<Message>> messages;
    Realm realm;
    RealmResults<Message> messageRealmResults;
    @Inject
    public User user;
    

   
    public ChatFragment() {
        super(R.layout.fragment_chat);
        this.messages = new MutableLiveData<>(new ArrayList<>());
        realm=Realm.getDefaultInstance();
        messageRealmResults =realm.where(Message.class).findAllAsync();
        messageRealmResults.addChangeListener((realmMessages, changeSet) -> messages.postValue(realm.copyFromRealm(realmMessages)));
        this.messages.setValue(messageRealmResults);
    }

   
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        binding.buttonGchatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(binding.editGchatMessage.getText().toString(),user.getId());
                realm.executeTransactionAsync(bgRealm -> {
                    bgRealm.copyToRealmOrUpdate(new Message());

                });
            }
        });
        return binding.getRoot();
    }
}
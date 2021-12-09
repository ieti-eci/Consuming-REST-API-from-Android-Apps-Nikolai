package com.example.secondclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.secondclass.databinding.FragmentFirstBinding;
import com.example.secondclass.network.RetrofitGenerator;
import com.example.secondclass.network.dto.LoginDto;
import com.example.secondclass.network.dto.TokenDto;
import com.example.secondclass.network.service.AuthService;
import com.example.secondclass.network.storage.SharedPreferencesStorage;
import com.example.secondclass.network.storage.Storage;

import retrofit2.Retrofit;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    //Storage Instance
    private Storage storage;

    //SharedPreferencesStorages
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(otherView -> {

            //Set the storage variable to not be null
            super.onViewCreated(view, savedInstanceState);
            sharedPreferences = requireContext().getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
            storage = new SharedPreferencesStorage(sharedPreferences);


            // Request the auth token
            sendAuthRequest();

        });
    }

    private void sendAuthRequest() {
        // Generate a instance
        Retrofit retrofit = RetrofitGenerator.getInstance(storage);
        AuthService authService = retrofit.create(AuthService.class);
        LoginDto loginDto = new LoginDto("santiago@mail.com","passw0rd");
        //If the case is Successfully
        Action1<TokenDto> successAction = tokenDto -> onSuccess(tokenDto.getAccessToken());
        //If the case is Failed
        Action1<Throwable> failedAction = throwable -> Log.e("Developer", "Auth error", throwable);

        authService.auth(loginDto)
                .observeOn(Schedulers.from(ContextCompat.getMainExecutor(requireContext())))
                .subscribe(successAction, failedAction);
    }

    // Function that makes the logic if the auth is Successfully
    private void onSuccess(String token) {
        Log.d("Developer", "Token: " + token);
        binding.textviewFirst.setText(token);
        storage.saveToken(token);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
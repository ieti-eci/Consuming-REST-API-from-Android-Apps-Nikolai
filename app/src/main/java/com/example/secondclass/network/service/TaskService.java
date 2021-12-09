package com.example.secondclass.network.service;

import java.util.List;

import okhttp3.internal.concurrent.Task;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskService {

    @GET("api/task/all")
    List<Task> all();

    @POST("api/task")
    Task create(@Body Task task);

    @GET("api/task/{id}")
    Task findById(@Body String id);

    @DELETE("api/task/{id}")
    boolean deleteById(@Path("id") String id);

    @PUT("api/task/{id}")
    Task update(@Body Task task, @Path("id") String id);
}

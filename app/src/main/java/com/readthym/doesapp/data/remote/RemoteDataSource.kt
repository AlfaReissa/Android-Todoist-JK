package com.readthym.doesapp.data.remote

import com.readthym.doesapp.data.remote.service.ApiService

class RemoteDataSource(
    private val commonService: ApiService,
) {

    suspend fun login(email: String, password: String) =
        commonService.login(email = email, password = password)

    suspend fun register(name: String, email: String, password: String) =
        commonService.register(name = name, email = email, password = password)

    suspend fun getTask(id: String) =
        commonService.getTaskUserById(id)

    suspend fun getDetailTask(id: String) =
        commonService.detailTaskTask(id)

    suspend fun deleteTask(id: String) =
        commonService.deleteTask(id)

    suspend fun saveNewTask(id_user:String,title:String,desc:String,deadline:String) =
        commonService.saveTask(id_user, title, desc, deadline)

    suspend fun updateTask(id:String,title:String,desc:String,deadline:String) =
        commonService.updateTask(id, title, desc, deadline)


}
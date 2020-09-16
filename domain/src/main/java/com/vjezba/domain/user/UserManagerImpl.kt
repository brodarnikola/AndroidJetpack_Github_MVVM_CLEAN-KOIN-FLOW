/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.domain.user

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vjezba.domain.repository.UserManager
import com.vjezba.domain.storage.Storage


private const val REGISTERED_USER = "registered_user"
private const val LOGGED_USER = "logged_user"
private const val PASSWORD_SUFFIX = "password"

/**
 * Handles User lifecycle. Manages registrations, logs in and logs out.
 * Knows when the user is logged in.
 */
//@Singleton
class UserManagerImpl constructor(private val storage: Storage,
                                        // Since UserManager will be in charge of managing the UserComponent lifecycle,
                                        // it needs to know how to create instances of it
                                      //private val userComponentFactory: UserComponent.Factory
                                ) : UserManager {





    override fun getUserManager(): UserManagerImpl {
        return this
    }


    




    val username: String
        get() = storage.getString(REGISTERED_USER)

    override fun isUserLoggedIn() = storage.getString(LOGGED_USER).isNotEmpty() //userComponent != null

    override fun isUserRegistered() = storage.getString(REGISTERED_USER).isNotEmpty()

    override fun getUserName(): String {
        return username
    }

    override fun registerUser(username: String, password: String) {
        storage.setString(LOGGED_USER, username)
        storage.setString(REGISTERED_USER, username)
        storage.setString("$username$PASSWORD_SUFFIX", password)
    }

    override fun loginUser(username: String, password: String): Boolean {
        val registeredUser = this.username
        if (registeredUser != username) return false

        val registeredPassword = storage.getString("$username$PASSWORD_SUFFIX")
        if (registeredPassword != password) return false

        storage.setString(LOGGED_USER, username)
        userJustLoggedIn()
        return true
    }

    override fun logout() {
        storage.setString(LOGGED_USER, "")
        //userComponent = null
    }

    override fun unregister() {
        val username = storage.getString(REGISTERED_USER)
        storage.setString(REGISTERED_USER, "")
        storage.setString("$username$PASSWORD_SUFFIX", "")
        storage.setString(LOGGED_USER, "")
    }
}
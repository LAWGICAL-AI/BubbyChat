package com.lawgicalai.bubbychat.data.model

import com.google.firebase.auth.FirebaseUser
import com.lawgicalai.bubbychat.domain.model.User

fun FirebaseUser.toUser(): User =
    User(
        profileImage = photoUrl?.toString(),
        email = email,
        displayName = displayName,
    )

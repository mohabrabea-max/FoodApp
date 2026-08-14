package com.example.applicationhome.core.domain.exception

import com.example.applicationhome.data.data.model.AuthError
import com.example.applicationhome.data.data.model.ErrorsType

class AuthException(val error: AuthError) : Exception()

class AppDomainException(val errorType: ErrorsType) : Exception(errorType.name)
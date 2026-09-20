package com.example.applicationhome.core.domain.exception

import com.example.applicationhome.core.domain.model.AuthError
import com.example.applicationhome.core.domain.model.ErrorsType

class AuthException(val error: AuthError) : Exception()

class AppDomainException(val errorType: ErrorsType) : Exception(errorType.name)
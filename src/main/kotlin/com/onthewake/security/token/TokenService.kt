package com.onthewake.security.token

interface TokenService {
    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}
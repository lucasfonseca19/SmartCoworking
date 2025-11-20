package com.example.smartcoworking.utils

import java.security.MessageDigest

object SecurityUtils {
    /**
     * Gera um hash SHA-256 da senha fornecida.
     *
     * Este método implementa o requisito de segurança de criptografia de senha
     * utilizando o algoritmo SHA-256, garantindo que as senhas não sejam
     * armazenadas em texto plano.
     *
     * @param senha A senha em texto plano
     * @return O hash da senha em formato hexadecimal
     */
    fun hashSenha(senha: String): String {
        val bytes = senha.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Verifica se a senha fornecida corresponde ao hash armazenado.
     *
     * @param senha A senha em texto plano
     * @param hashArmazenado O hash armazenado para comparação
     * @return true se a senha corresponder, false caso contrário
     */
    fun verificarSenha(senha: String, hashArmazenado: String): Boolean {
        val hashCalculado = hashSenha(senha)
        return hashCalculado == hashArmazenado
    }
}

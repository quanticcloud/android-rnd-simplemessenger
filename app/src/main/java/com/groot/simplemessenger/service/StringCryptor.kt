package com.groot.simplemessenger.service

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec


private const val CIPHER_ALGORITHM = "AES"
private const val RANDOM_GENERATOR_ALGORITHM = "SHA1PRNG"
private const val RANDOM_KEY_SIZE = 128
object StringCryptor {
    // Encrypts string and encode in Base64
    @Throws(Exception::class)
    fun encrypt(password: String, data: String): String {
        val secretKey = generateKey(password.toByteArray())
        val clear = data.toByteArray()
        val secretKeySpec = SecretKeySpec(secretKey, CIPHER_ALGORITHM)
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encrypted = cipher.doFinal(clear)
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    // Decrypts string encoded in Base64
    @Throws(Exception::class)
    fun decrypt(password: String, encryptedData: String?): String {
        val secretKey = generateKey(password.toByteArray())
        val secretKeySpec = SecretKeySpec(secretKey, CIPHER_ALGORITHM)
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val encrypted: ByteArray = Base64.decode(encryptedData, Base64.DEFAULT)
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }

    @Throws(Exception::class)
    fun generateKey(seed: ByteArray?): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(CIPHER_ALGORITHM)
        val secureRandom = SecureRandom.getInstance(RANDOM_GENERATOR_ALGORITHM)
        secureRandom.setSeed(seed)
        keyGenerator.init(RANDOM_KEY_SIZE, secureRandom)
        val secretKey = keyGenerator.generateKey()
        return secretKey.encoded
    }

}
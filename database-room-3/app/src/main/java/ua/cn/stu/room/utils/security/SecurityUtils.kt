package ua.cn.stu.room.utils.security

interface SecurityUtils {

    /**
     * Generate random salt using secure random generator
     */
    fun generateSalt(): ByteArray

    /**
     * Generate hash from password securely
     */
    fun passwordToHash(password: CharArray, salt: ByteArray): ByteArray

    /**
     * Convert byte array into string. It's just a conversion (e.g. into Base64), not encryption.
     */
    fun bytesToString(bytes: ByteArray): String

    /**
     * Convert string back to byte array. It's just a conversion (e.g. from Base64), not decryption.
     */
    fun stringToBytes(string: String): ByteArray

}
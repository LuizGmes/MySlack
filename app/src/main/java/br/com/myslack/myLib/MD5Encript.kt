package br.com.myslack.myLib


import java.security.MessageDigest

class MD5Encript {
    companion object {
        private fun byteArrayToHexString( array: Array<Byte> ): String {
            var result = StringBuilder(array.size * 2)

            for ( byte in array ) {
                val toAppend =
                    String.format("%2X", byte).replace(" ", "0") // hexadecimal
                result.append(toAppend).append("-")
            }
            result.setLength(result.length - 1) // remove last '-'
            return result.toString()
        }

        fun encrypt( text: String ): String {
            try {
                val md5 = MessageDigest.getInstance("MD5")
                val md5HashBytes = md5.digest(text.toByteArray()).toTypedArray()
                return byteArrayToHexString(md5HashBytes)
            } catch ( e: Exception ) {
                return "error: ${e.message}"
            }
        }
    }
}


package com.phoneCalls.utils

object PhoneCallConstants {

  object values {
    //constants declaration
    val MULT_LESS_THAN_5: Double = 3
    val MULT_AT_LEAST_5: Double = 150
  }

  object test {
    // Auxiliary constants used in tests
    val TEST_STRING: String =
      "00:01:07,400-234-090" +
        "00:05:01,701-080-080" +
        "00:05:00,400-234-090"

    // Auxiliary constants used in process
    val STRING_CALL_VALUES: String =
      "00:01:07,400-234-090" +
        "00:05:01,701-080-080" +
        "00:05:00,400-234-090"

    val TEST_PHONE_CALL: String = "00:05:01,701-080-080"
  }
}

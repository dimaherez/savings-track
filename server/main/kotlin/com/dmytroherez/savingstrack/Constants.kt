package com.dmytroherez.savingstrack

object Constants {
    // Auth
    const val FIREBASE_PROJECT_ID = "savingstrack-669e8"
    const val JWK_PROVIDER_URL = "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com"
    const val JWT_NAME = "firebase-auth"
    const val JWT_PROVIDER_ISSUER_URL = "https://securetoken.google.com/$FIREBASE_PROJECT_ID"

    // Tables
    const val TABLE_SAVINGS = "Savings"

    // Table fields
    const val FIELD_ID = "id"
    const val FIELD_USER_ID = "userId"
    const val FIELD_CURRENCY = "currency"
    const val FIELD_AMOUNT = "amount"
    const val FIELD_DESCRIPTION = "description"
    const val FIELD_CATEGORY = "category"
    const val FIELD_CREATED_AT = "createdAt"


}
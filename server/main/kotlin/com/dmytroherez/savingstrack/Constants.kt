package com.dmytroherez.savingstrack

object Constants {
    // Auth
    const val FIREBASE_PROJECT_ID = "savingstrack-669e8"
    const val JWK_PROVIDER_URL = "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com"
    const val JWT_NAME = "firebase-auth"
    const val JWT_PROVIDER_ISSUER_URL = "https://securetoken.google.com/$FIREBASE_PROJECT_ID"

    // Tables
    const val TABLE_TRANSACTIONS = "Transactions"
    const val TABLE_GOALS = "Goals"

    // Table fields

    //id
    const val FIELD_ID = "id"
    const val FIELD_GOAL_ID = "goalId"
    const val FIELD_FIREBASE_UID = "firebaseUid"

    // amount
    const val FIELD_AMOUNT = "amount"
    const val FIELD_TARGET_AMOUNT = "amount"

    // dates
    const val FIELD_CREATED_AT = "createdAt"
    const val FIELD_DEADLINE = "deadline"
    const val FIELD_COMPLETED_AT = "deadline"

    // title and description
    const val FIELD_TITLE = "title"
    const val FIELD_DESCRIPTION = "description"

    // currency
    const val FIELD_CURRENCY = "currency"
    const val FIELD_CATEGORY = "category"
}
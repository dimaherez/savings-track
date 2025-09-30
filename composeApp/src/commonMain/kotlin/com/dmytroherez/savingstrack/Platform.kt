package com.dmytroherez.savingstrack

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
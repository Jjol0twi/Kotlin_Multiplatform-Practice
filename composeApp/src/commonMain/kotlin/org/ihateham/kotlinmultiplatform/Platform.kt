package org.ihateham.kotlinmultiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
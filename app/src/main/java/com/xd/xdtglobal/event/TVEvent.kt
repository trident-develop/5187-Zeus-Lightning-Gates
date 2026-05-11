package com.xd.xdtglobal.event

sealed interface TVEvent {
    data object OpenGame : TVEvent
}
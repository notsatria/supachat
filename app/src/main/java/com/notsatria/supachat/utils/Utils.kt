package com.notsatria.supachat.utils

import kotlinx.coroutines.Dispatchers

val dispatcher = Dispatchers.IO.limitedParallelism(1)
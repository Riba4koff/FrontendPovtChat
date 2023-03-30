package com.example.chatapp.domain.UseCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UseCase<in Param, out Data> {
    suspend operator fun invoke(param: Param): Data
}
interface FlowUseCase<in Param, out Data>: UseCase<Param, Flow<Data>>
package com.mundocode.pomodoro.data.habitsDB.domain

import com.mundocode.pomodoro.data.habitsDB.HabitsRepository
import com.mundocode.pomodoro.ui.screens.habits.model.HabitsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUserCase @Inject constructor(private val habitRepository: HabitsRepository) {

    operator fun invoke(): Flow<List<HabitsModel>> = habitRepository.habits
}

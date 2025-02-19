package com.mundocode.pomodoro.data.habitsDB.domain

import com.mundocode.pomodoro.data.habitsDB.HabitsRepository
import com.mundocode.pomodoro.ui.screens.habits.model.HabitsModel
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val habitsRepository: HabitsRepository) {

    suspend operator fun invoke(habitModel: HabitsModel) {
        habitsRepository.deleteHabit(habitModel)
    }
}

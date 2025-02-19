package com.mundocode.pomodoro.data.habitsDB.domain

import com.mundocode.pomodoro.data.habitsDB.HabitsRepository
import com.mundocode.pomodoro.ui.screens.habits.model.HabitsModel
import javax.inject.Inject

class AddTaskUserCase @Inject constructor(private val habitRepository: HabitsRepository) {

    suspend operator fun invoke(habitModel: HabitsModel) {
        habitRepository.addHabit(habitModel)
    }
}

package com.mundocode.pomodoro.data.habitsDB

import com.mundocode.pomodoro.ui.screens.habits.model.HabitsModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class HabitsRepository @Inject constructor(private val habitsDao: HabitsDao) {

    val habits: Flow<List<HabitsModel>> = habitsDao.getHabits().map { list ->
        list.map { HabitsModel(it.id, it.title, it.description) }
    }

    suspend fun addHabit(habit: HabitsModel) {
        habitsDao.addHabit(habit.toData())
    }

    suspend fun updateHabit(habit: HabitsModel) {
        habitsDao.updateHabit(habit.toData())
    }

    suspend fun deleteHabit(habit: HabitsModel) {
        habitsDao.deleteHabit(habit.toData())
    }
}

fun HabitsModel.toData(): HabitsEntity = HabitsEntity(this.id, this.title, this.description)

package com.mundocode.pomodoro.data.habitsDB

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mundocode.pomodoro.ui.screens.habits.model.HabitsModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HabitsRepository @Inject constructor(
    private val habitsDao: HabitsDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private val userId: String? get() = auth.currentUser?.uid

    val habits: Flow<List<HabitsModel>> = habitsDao.getHabits().map { habitsEntityList ->
        habitsEntityList.map { habitsEntity ->
            HabitsModel(
                id = habitsEntity.id,
                title = habitsEntity.title,
                description = habitsEntity.description,
            )
        }
    }

    suspend fun addHabit(habit: HabitsModel) {
        val existingHabit = habitsDao.getHabitById(habit.id) // ✅ Método correcto en DAO
        if (existingHabit == null) { // ✅ Solo insertar si el hábito no existe
            habitsDao.addHabit(habit.toData())
            syncHabitWithFirestore(habit)
        }
    }

    suspend fun updateHabit(habit: HabitsModel) {
        habitsDao.updateHabit(habit.toData())
        syncHabitWithFirestore(habit)
    }

    suspend fun deleteHabit(habit: HabitsModel) {
        habitsDao.deleteHabit(habit.toData())
        deleteHabitFromFirestore(habit)
    }

    private fun syncHabitWithFirestore(habit: HabitsModel) {
        userId?.let { uid ->
            val habitRef = firestore.collection("users").document(uid)
                .collection("habits").document(habit.id.toString())
            habitRef.set(habit)
                .addOnSuccessListener {
                    println("Habit successfully added to Firestore")
                }
                .addOnFailureListener { e ->
                    println("Error adding habit to Firestore: $e")
                }
        }
    }

    private fun deleteHabitFromFirestore(habit: HabitsModel) {
        userId?.let { uid ->
            val habitRef = firestore.collection("users").document(uid)
                .collection("habits").document(habit.id.toString())
            habitRef.delete()
                .addOnSuccessListener {
                    println("Habit successfully deleted from Firestore")
                }
                .addOnFailureListener { e ->
                    println("Error deleting habit from Firestore: $e")
                }
        }
    }

    fun syncFromFirestore(scope: CoroutineScope, searchQuery: String) {
        userId?.let { uid ->
            val collectionRef = firestore.collection("users").document(uid).collection("habits")
            var query: Query = collectionRef.orderBy("title", Query.Direction.ASCENDING)

            if (searchQuery.isNotEmpty()) {
                query = query.whereGreaterThanOrEqualTo("title", searchQuery)
                    .whereLessThanOrEqualTo("title", searchQuery + "\uf8ff")
            }

            query.addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener

                scope.launch {
                    val habitsList = snapshot.documents.mapNotNull { it.toObject(HabitsModel::class.java) }

                    habitsList.forEach { habit ->
                        val existingHabit = habitsDao.getHabits().firstOrNull()?.find { it.id == habit.id }
                        if (existingHabit == null) { // ✅ Solo insertar si no existe
                            habitsDao.addHabit(habit.toData())
                        } else {
                            habitsDao.updateHabit(habit.toData()) // ✅ Si existe, actualizarlo en lugar de insertarlo
                        }
                    }
                }
            }
        }
    }
}

fun HabitsModel.toData(): HabitsEntity = HabitsEntity(this.id, this.title, this.description)

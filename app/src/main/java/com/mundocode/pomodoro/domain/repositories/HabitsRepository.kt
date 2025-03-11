package com.mundocode.pomodoro.domain.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mundocode.pomodoro.core.room.dao.HabitsDao
import com.mundocode.pomodoro.model.local.Habits
import com.mundocode.pomodoro.model.toDto
import com.mundocode.pomodoro.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HabitsRepository @Inject constructor(
    private val habitsDao: HabitsDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private val userId: String? get() = auth.currentUser?.uid

    fun getHabits(): Flow<List<Habits>> = habitsDao.getHabits().map { habitsEntityList ->
        habitsEntityList.map { it.toDto() }
    }

    suspend fun addHabit(habit: Habits) {
        val existingHabit = habitsDao.getHabitById(habit.id) // ✅ Método correcto en DAO
        if (existingHabit == null) { // ✅ Solo insertar si el hábito no existe
            habitsDao.insert(habit.toEntity())
            syncHabitWithFirestore(habit)
        }
    }

    suspend fun updateHabit(habit: Habits) {
        habitsDao.update(habit.toEntity())
        syncHabitWithFirestore(habit)
    }

    suspend fun deleteHabit(habit: Habits) {
        habitsDao.delete(habit.toEntity())
        deleteHabitFromFirestore(habit)
    }

    private fun syncHabitWithFirestore(habit: Habits) {
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

    private fun deleteHabitFromFirestore(habit: Habits) {
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

                scope.launch(Dispatchers.IO) {
                    // Use injected CoroutineScope
                    for (docChange in snapshot.documentChanges) {
                        val habit = docChange.document.toObject(Habits::class.java)

                        when (docChange.type) {
                            DocumentChange.Type.ADDED -> {
                                Timber.d("User added: ${habit.title}")
                                habitsDao.insert(habit.toEntity())
                            }

                            DocumentChange.Type.MODIFIED -> {
                                Timber.d("User updated: ${habit.title}")
                                habitsDao.update(habit.toEntity())
                            }

                            DocumentChange.Type.REMOVED -> {
                                Timber.d("User deleted: ${habit.title}")
                                habitsDao.delete(habit.toEntity())
                            }
                        }
                    }
                }
            }
        }
    }
}

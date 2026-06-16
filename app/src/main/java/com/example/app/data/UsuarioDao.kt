package com.example.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    // Para la pantalla de Registro: guarda el nuevo usuario
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarUsuario(usuario: Usuario): Long

    // Para la pantalla de Login: busca si coincide el email y la contraseña
    @Query("SELECT * FROM usuarios WHERE email = :email AND contrasenia = :contrasenia LIMIT 1")
    suspend fun login(email: String, contrasenia: String): Usuario?

    // Para Home y Perfil: obtiene los datos del usuario logueado en tiempo real
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    fun obtenerUsuarioPorId(id: Int): Flow<Usuario?>
}
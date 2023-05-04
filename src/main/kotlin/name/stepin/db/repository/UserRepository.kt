package name.stepin.db.repository

import kotlinx.coroutines.flow.Flow
import name.stepin.db.entity.UserEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository : ReactiveCrudRepository<UserEntity, Long> {
    suspend fun findByDisplayName(name: String): UserEntity

    fun findAllByEmailContains(contains: String = "@example.com"): Flow<UserEntity>
}

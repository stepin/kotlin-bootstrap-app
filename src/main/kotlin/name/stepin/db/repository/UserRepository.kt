package name.stepin.db.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import name.stepin.db.entity.UserEntity

@ApplicationScoped
class UserRepository : PanacheRepository<UserEntity> {
    suspend fun findByName(name: String) = find("name", name).firstResult().awaitSuspending()

    suspend fun findFromExampleCom() = list("email LIKE ?1", "%@example.com").awaitSuspending()
}

package name.stepin.fixture

import name.stepin.db.entity.UserEntity
import java.time.LocalDate
import java.util.*

object UserEntityFactory {
    fun userEntity(id: Long = 1) =
        UserEntity().apply {
            this.id = id
            guid = UUID.randomUUID().toString()
            accountId = 1000 + id
            accountGuid = UUID.randomUUID().toString()
            displayName = "displayName$id"
            firstName = "firstName$id"
            lastName = "lastName$id"
            password = "password$id"
            salt = "salt$id"
            email = "me$id@example.com"
            createdAt = LocalDate.now()
        }
}

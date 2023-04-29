package name.stepin

import name.stepin.db.sql.tables.records.UsersRecord
import java.time.LocalDateTime
import java.util.*

object UsersRecordFactory {

    fun usersRecord(id: Long = 1) = UsersRecord(
        id = id,
        guid = UUID.randomUUID(),
        accountId = 1000 + id,
        accountGuid = UUID.randomUUID(),
        displayName = "displayName$id",
        firstName = "firstName$id",
        lastName = "lastName$id",
        password = "password$id",
        salt = "salt$id",
        email = "me$id@example.com",
        createdAt = LocalDateTime.now()
    )
}

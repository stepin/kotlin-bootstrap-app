package name.stepin.db.dao

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.NotFoundException
import kotlinx.coroutines.future.await
import name.stepin.db.sql.tables.records.UsersRecord
import name.stepin.db.sql.tables.references.USERS
import org.apache.logging.log4j.kotlin.Logging
import org.jooq.DSLContext
import org.jooq.exception.TooManyRowsException

@ApplicationScoped
class UsersDao(
    private val db: DSLContext
) {
    companion object : Logging

    suspend fun getAll(): List<UsersRecord> {
        val result = db.fetchAsync(USERS).await()
        return result.toList()
    }

    suspend fun byId(id: Long): UsersRecord? {
        val result = db.fetchAsync(USERS, USERS.ID.eq(id)).await()
        if (result.size > 1) {
            throw TooManyRowsException()
        }
        if (result.size == 0) {
            throw NotFoundException()
        }
        return result.first()
    }

    suspend fun byIdNullable(id: Long): UsersRecord? {
        val result = db.fetchAsync(USERS, USERS.ID.eq(id)).await()
        if (result.size > 1) {
            throw TooManyRowsException()
        }
        return result.firstOrNull()
    }

    suspend fun newRecord(): UsersRecord {
        return db.newRecord(USERS)
    }
}

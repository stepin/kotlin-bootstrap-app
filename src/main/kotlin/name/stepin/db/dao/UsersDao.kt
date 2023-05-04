package name.stepin.db.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import name.stepin.db.sql.tables.records.UsersRecord
import name.stepin.db.sql.tables.references.USERS
import name.stepin.exception.EntityNotFoundException
import org.apache.logging.log4j.kotlin.Logging
import org.jooq.DSLContext
import org.jooq.exception.TooManyRowsException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class UsersDao(
    private val db: DSLContext,
    // NOTE: for some reason fetchAsync don't use R2DBC, it uses JDBC, but there is way with Flux/Mono
    // jdbcDb here is only as example
    @Qualifier("jdbcDb")
    private val jdbcDb: DSLContext,
) {
    companion object : Logging

    fun getAllEmails(): Flow<String?> {
        return Flux.from(
            db
                .select(USERS.EMAIL)
                .from(USERS),
        )
            .asFlow()
            .map { it.value1() }
    }

    suspend fun getAll(): List<UsersRecord> {
        return jdbcDb.fetchAsync(USERS).await()
    }

    suspend fun byId(id: Long): UsersRecord {
        val result = jdbcDb.fetchAsync(USERS, USERS.ID.eq(id)).await()
        if (result.size > 1) {
            throw TooManyRowsException()
        }
        if (result.size == 0) {
            throw EntityNotFoundException()
        }
        return result.first()
    }

    suspend fun byIdNullable(id: Long): UsersRecord? {
        return Mono.from(
            db.select(USERS).from(USERS).where(USERS.ID.eq(id)),
        ).map { it.value1() }.awaitSingleOrNull()
    }

    fun newRecord(): UsersRecord {
        return jdbcDb.newRecord(USERS)
    }
}

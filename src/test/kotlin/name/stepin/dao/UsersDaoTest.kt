package name.stepin.dao

import kotlinx.coroutines.runBlocking
import name.stepin.PostgresFactory.dslContext
import name.stepin.PostgresFactory.initDb
import name.stepin.PostgresFactory.postgres
import name.stepin.UsersRecordFactory
import name.stepin.db.dao.UsersDao
import name.stepin.db.sql.tables.records.UsersRecord
import name.stepin.db.sql.tables.references.USERS
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
internal class UsersDaoTest {

    @Container
    var postgres = postgres()

    private lateinit var dao: UsersDao
    private lateinit var db: DSLContext

    @BeforeEach
    fun setUp() {
        initDb(postgres)
        db = dslContext(postgres)
        dao = UsersDao(db)
    }

    @Test
    fun `getAll main case`() = runBlocking {
        val user0: UsersRecord = UsersRecordFactory.usersRecord(1)
        val user1 = db.newRecord(USERS)
        user1.email = user0.email
        user1.id = user0.id
        user1.guid = user0.guid
        user1.accountId = user0.accountId
        user1.accountGuid = user0.accountGuid
        user1.password = user0.password
        user1.salt = user0.salt
        user1.createdAt = user0.createdAt
        user1.store()

        val actual = dao.getAll()

        assertEquals(1, actual.size)
    }
}

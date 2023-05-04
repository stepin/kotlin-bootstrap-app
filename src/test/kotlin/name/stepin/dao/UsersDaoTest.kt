package name.stepin.dao

import kotlinx.coroutines.runBlocking
import name.stepin.db.dao.UsersDao
import name.stepin.db.sql.tables.records.UsersRecord
import name.stepin.db.sql.tables.references.USERS
import name.stepin.exception.EntityNotFoundException
import name.stepin.fixture.PostgresFactory.dslContext
import name.stepin.fixture.PostgresFactory.initDb
import name.stepin.fixture.PostgresFactory.postgres
import name.stepin.fixture.UsersRecordFactory
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
internal class UsersDaoTest {

    @Container
    var postgres = postgres()

    private lateinit var dao: UsersDao
    private lateinit var db: DSLContext
    private lateinit var jdbcContext: DSLContext

    @BeforeEach
    fun setUp() {
        initDb(postgres)
        db = dslContext(postgres)
        jdbcContext = dslContext(postgres)
        dao = UsersDao(db, jdbcContext)
        initDb()
    }

    private fun initDb() {
        db.delete(USERS).execute()
        createUser(1)
        createUser(2)
    }

    private fun createUser(id: Long) {
        val user: UsersRecord = UsersRecordFactory.usersRecord(id)
        val dbRecord = db.newRecord(USERS)
        dbRecord.email = user.email
        dbRecord.id = user.id
        dbRecord.guid = user.guid
        dbRecord.accountId = user.accountId
        dbRecord.accountGuid = user.accountGuid
        dbRecord.password = user.password
        dbRecord.salt = user.salt
        dbRecord.createdAt = user.createdAt
        dbRecord.store()
    }

    @Test
    fun `getAllEmails main case`() = runBlocking {
        val actual = buildString { dao.getAllEmails().collect { append(it).append(", ") } }

        assertEquals("me1@example.com, me2@example.com, ", actual)
    }

    @Test
    fun `getAll main case`() = runBlocking {
        val actual = dao.getAll()

        assertEquals("1,2", actual.joinToString(",") { it.id.toString() })
    }

    @Test
    fun `byId not found case`(): Unit = runBlocking {
        assertThrows<EntityNotFoundException> { dao.byId(3) }
    }

    @Test
    fun `byId main case`() = runBlocking {
        val actual = dao.byId(2)
        assertEquals(2, actual.id)
    }

    @Test
    fun `byIdNullable not found case`(): Unit = runBlocking {
        val actual = dao.byIdNullable(3)
        assertEquals(null, actual)
    }

    @Test
    fun `byIdNullable main case`() = runBlocking {
        val actual = dao.byIdNullable(2)
        assertEquals(2, actual?.id)
    }

    @Test
    fun `newRecord main case`() {
        val actual = dao.newRecord()
        assertEquals(null, actual.id)
    }
}

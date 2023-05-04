package name.stepin.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDate

@Table("users")
class UserEntity : Serializable {
    @Id
    var id: Long? = null
    lateinit var guid: String

    @Column("account_id")
    var accountId: Long = 0

    @Column("account_guid")
    var accountGuid: String? = null

    @Column("display_name")
    var displayName: String? = null

    @Column("first_name")
    var firstName: String? = null

    @Column("last_name")
    var lastName: String? = null
    lateinit var password: String
    lateinit var salt: String
    lateinit var email: String
    lateinit var createdAt: LocalDate

    override fun toString(): String {
        return "UserEntity{$email}"
    }
}

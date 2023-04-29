package name.stepin.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
class UserEntity {
    @Id
    @GeneratedValue
    var id: Long? = null
    lateinit var guid: String

    @Column(name = "account_id")
    var accountId: Long = 0

    @Column(name = "account_guid")
    var accountGuid: String? = null

    @Column(name = "display_name")
    var displayName: String? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null
    lateinit var password: String
    lateinit var salt: String
    lateinit var email: String
    lateinit var createdAt: LocalDate
}

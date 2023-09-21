package br.com.generic.base.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.generic.base.utils.Constants.Companion.USER_DATA

@Entity(tableName = USER_DATA)
data class RecordedUserData(
    @PrimaryKey(autoGenerate = false)
    var userId   : Int,
    var userName : String
)

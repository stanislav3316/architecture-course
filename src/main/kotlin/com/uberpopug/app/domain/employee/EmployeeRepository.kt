package com.uberpopug.app.domain.employee

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CrudRepository<Employee, String> {

    @Query("SELECT COUNT(e) from Employee e")
    fun getEmployeeAmount(): Int

    @Query("SELECT * FROM Employee e LIMIT :limit OFFSET :offset", nativeQuery = true)
    fun findALlWithOffsetAndLimit(
        @Param("limit") limit: Int,
        @Param("offset") offset: Int
    ): List<Employee>
}

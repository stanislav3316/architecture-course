package com.uberpopug.employee.config.auth

import com.uberpopug.employee.domain.EmployeeNotFound
import com.uberpopug.employee.domain.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailService : UserDetailsService {

    @Autowired
    private lateinit var employeeService: EmployeeService

    override fun loadUserByUsername(phone: String): UserDetails {

        val employee = try {
            employeeService.showForAuthentication(phone)
        } catch (e: EmployeeNotFound) {
            throw UsernameNotFoundException("by phone - $phone")
        }

        return CustomUserDetail(
            employee.employeeId!!,
            employee.phoneNumber,
            employee.firstName,
            employee.lastName,
            employee.loginPassword,
            employee.role.name
        )
    }
}

data class CustomUserDetail(
    val id: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val userPassword: String,
    val role: String
) : UserDetails {

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(role))
    override fun getPassword() = userPassword
    override fun getUsername() = phoneNumber
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}

package com.uberpopug.app.config.auth

import com.uberpopug.app.employee.EmployeeNotFound
import com.uberpopug.app.employee.EmployeeService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailService(private var employeeService: EmployeeService) : UserDetailsService {

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
            "admin",
            "admin"
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

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null
    override fun getPassword(): String = userPassword
    override fun getUsername(): String = phoneNumber
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

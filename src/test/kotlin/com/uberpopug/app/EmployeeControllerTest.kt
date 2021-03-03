package com.uberpopug.app

import com.jayway.jsonpath.JsonPath
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `create new employee`() {
        mockMvc.post("/v1/employee") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "firstName": "Stanislav",
                    "lastName": "Bolsun",
                    "phoneNumber": "+79999999999"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
            jsonPath("$.employeeId", notNullValue())
            jsonPath("$.firstName", equalTo("Stanislav"))
            jsonPath("$.lastName", equalTo("Bolsun"))
            jsonPath("$.phoneNumber", equalTo("+79999999999"))
        }
    }

    @Test
    fun `get employee by id`() {
        val employeeJsonData = mockMvc.post("/v1/employee") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "firstName": "Stanislav",
                    "lastName": "Bolsun",
                    "phoneNumber": "+79999999999"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        val employeeId = JsonPath.read<String>(employeeJsonData.response.contentAsString, "$.employeeId")
        mockMvc.get("/v1/employee/{employeeId}", employeeId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.employeeId", equalTo(employeeId))
            jsonPath("$.firstName", equalTo("Stanislav"))
            jsonPath("$.lastName", equalTo("Bolsun"))
            jsonPath("$.phoneNumber", equalTo("+79999999999"))
        }
    }
}

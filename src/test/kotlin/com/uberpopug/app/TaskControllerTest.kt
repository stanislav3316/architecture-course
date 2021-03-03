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
class TaskControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `crate new task`() {
        mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make pg dump",
                    "description": "we need to make pg dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
            jsonPath("$.taskId", notNullValue())
            jsonPath("$.title", equalTo("make pg dump"))
            jsonPath("$.description", equalTo("we need to make pg dump"))
            jsonPath("$.createdByEmployeeId", equalTo("1"))
            jsonPath("$.status", equalTo("NEW"))
        }
    }

    @Test
    fun `get task by id`() {
        val taskJsonData = mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make pg dump",
                    "description": "we need to make pg dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        val taskId = JsonPath.read<String>(taskJsonData.response.contentAsString, "$.taskId")
        mockMvc.get("/v1/task/{taskId}", taskId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.taskId", equalTo(taskId))
            jsonPath("$.title", equalTo("make pg dump"))
            jsonPath("$.description", equalTo("we need to make pg dump"))
            jsonPath("$.createdByEmployeeId", equalTo("1"))
            jsonPath("$.status", equalTo("NEW"))
        }
    }

    @Test
    fun `assign all opened tasks for one employee`() {
        val firstTaskJsonData = mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make pg dump",
                    "description": "we need to make pg dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()
        val firstTaskId = JsonPath.read<String>(firstTaskJsonData.response.contentAsString, "$.taskId")

        val secondTaskJsonData = mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make mysql dump",
                    "description": "we need to make mysql dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()
        val secondTaskId = JsonPath.read<String>(secondTaskJsonData.response.contentAsString, "$.taskId")

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

        mockMvc.post("/v1/task/assign") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/v1/task/assignedFor/{employeeId}", employeeId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                json("""
                    [
                        "$firstTaskId",
                        "$secondTaskId"
                    ]
                """.trimIndent())
            }
        }
    }

    @Test
    fun `assign all opened tasks and tasks are put assignedToEmployeeId inside`() {
        val firstTaskJsonData = mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make pg dump",
                    "description": "we need to make pg dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()
        val firstTaskId = JsonPath.read<String>(firstTaskJsonData.response.contentAsString, "$.taskId")

        val secondTaskJsonData = mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make mysql dump",
                    "description": "we need to make mysql dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()
        val secondTaskId = JsonPath.read<String>(secondTaskJsonData.response.contentAsString, "$.taskId")

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

        mockMvc.post("/v1/task/assign") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/v1/task/{taskId}", firstTaskId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.taskId", equalTo(firstTaskId))
            jsonPath("$.assignedToEmployeeId", equalTo(employeeId))
            jsonPath("$.status", equalTo("IN_PROGRESS"))
        }

        mockMvc.get("/v1/task/{taskId}", secondTaskId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.taskId", equalTo(secondTaskId))
            jsonPath("$.assignedToEmployeeId", equalTo(employeeId))
            jsonPath("$.status", equalTo("IN_PROGRESS"))
        }
    }

    @Test
    fun `complete task by employee`() {
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

        val taskJsonData = mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make pg dump",
                    "description": "we need to make pg dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()
        val taskId = JsonPath.read<String>(taskJsonData.response.contentAsString, "$.taskId")

        mockMvc.post("/v1/task/assign") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.post("/v1/task/{taskId}/complete", taskId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/v1/task/{taskId}", taskId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.taskId", equalTo(taskId))
            jsonPath("$.status", equalTo("COMPLETED"))
            jsonPath("$.assignedToEmployeeId", equalTo(employeeId))
        }
    }

    @Test
    fun `close created task`() {
        val taskJsonData = mockMvc.post("/v1/task") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "make pg dump",
                    "description": "we need to make pg dump",
                    "createdByEmployeeId": "1"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }.andReturn()
        val taskId = JsonPath.read<String>(taskJsonData.response.contentAsString, "$.taskId")

        mockMvc.post("/v1/task/{taskId}/close", taskId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/v1/task/{taskId}", taskId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.taskId", equalTo(taskId))
            jsonPath("$.status", equalTo("CLOSED"))
        }
    }
}

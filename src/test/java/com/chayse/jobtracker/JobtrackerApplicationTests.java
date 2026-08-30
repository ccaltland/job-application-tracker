package com.chayse.jobtracker;

import com.chayse.jobtracker.model.JobApplication;
import com.chayse.jobtracker.model.JobStatus;
import com.chayse.jobtracker.repository.JobApplicationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class JobtrackerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobApplicationRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

	   // 1. CREATE
    @Test
    void shouldCreateApplication() throws Exception {
        String json = """
            {
                "company": "Epic",
                "position": "Infrastructure Engineer",
                "status": "APPLIED",
                "dateApplied": "2026-08-27",
                "notes": "Completed assessment"
            }
            """;

        mockMvc.perform(post("/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Epic"))
                .andExpect(jsonPath("$.status").value("APPLIED"));
    }

    // 2. VALIDATION
    @Test
    void shouldRejectInvalidApplication() throws Exception {
        String json = """
            {
                "company": "",
                "position": "",
                "status": null
            }
            """;

        mockMvc.perform(post("/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

	// 3. NOT FOUND
	@Test
	void shouldReturn404ForMissingApplication() throws Exception {
		mockMvc.perform(get("/applications/99999"))
				.andExpect(status().isNotFound());
	}

	// 4. DYNAMIC FILTERING
	@Test
	void shouldFilterByCompanyAndStatus() throws Exception {

		JobApplication a1 = new JobApplication();
		a1.setCompany("Epic");
		a1.setPosition("Infrastructure Engineer");
		a1.setStatus(JobStatus.APPLIED);
		a1.setDateApplied(LocalDate.of(2026, 8, 27));

		JobApplication a2 = new JobApplication();
		a2.setCompany("Epic");
		a2.setPosition("Software Engineer");
		a2.setStatus(JobStatus.REJECTED);
		a2.setDateApplied(LocalDate.of(2026, 8, 20));

		JobApplication a3 = new JobApplication();
		a3.setCompany("IBM");
		a3.setPosition("Developer");
		a3.setStatus(JobStatus.APPLIED);
		a3.setDateApplied(LocalDate.of(2026, 8, 25));

		repository.saveAll(List.of(a1, a2, a3));

		mockMvc.perform(get("/applications")
				.param("company", "Epic")
				.param("status", "APPLIED"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))		
				.andExpect(jsonPath("$[0].company").value("Epic"))
				.andExpect(jsonPath("$[0].status").value("APPLIED"));
	}
	// 5. SORTING
	@Test
	void shouldSortByDateDescending() throws Exception {
		// save an older application
		// save a newer application
		JobApplication a1 = new JobApplication();
		a1.setCompany("Epic");
		a1.setPosition("Infrastructure Engineer");
		a1.setStatus(JobStatus.APPLIED);
		a1.setDateApplied(LocalDate.of(2026, 8, 27));

		JobApplication a2 = new JobApplication();
		a2.setCompany("Epic");
		a2.setPosition("Software Engineer");
		a2.setStatus(JobStatus.REJECTED);
		a2.setDateApplied(LocalDate.of(2026, 8, 20));

		repository.saveAll(List.of(a1, a2));

		mockMvc.perform(get("/applications")
				.param("sort", "DESC"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].dateApplied").value("2026-08-27"))
				.andExpect(jsonPath("$[1].dateApplied").value("2026-08-20"));
	}
	// 6. DELETE
	@Test
	void shouldDeleteApplication() throws Exception {
		// save one application
		JobApplication a1 = new JobApplication();
		a1.setCompany("Epic");
		a1.setPosition("Infrastructure Engineer");
		a1.setStatus(JobStatus.APPLIED);
		a1.setDateApplied(LocalDate.of(2026, 8, 27));
		// get its generated id
		JobApplication saved = repository.save(a1);
		Long id = saved.getId();

		mockMvc.perform(delete("/applications/" + id))
				.andExpect(status().isOk());		

		mockMvc.perform(get("/applications/" + id))
				.andExpect(status().isNotFound());
	}
}

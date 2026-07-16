package com.berkay.atm_simulation;

import com.berkay.atm_simulation.config.SecurityConfig;
import com.berkay.atm_simulation.controller.AdminController;
import com.berkay.atm_simulation.security.LoginFailureHandler;
import com.berkay.atm_simulation.service.AccountService;
import com.berkay.atm_simulation.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AdminService adminService; // AdminController dependency
    @MockitoBean
    AccountService accountService; // CurrentUserAdvice dependency
    @MockitoBean
    LoginFailureHandler failureHandler; // SecurityConfig dependency

    @Test
    @WithAnonymousUser
    void adminPage_redirectsAnonymousLogin() throws Exception {
        mockMvc.perform(get("/admin/accounts"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void adminPage_forbiddenForUser() throws Exception {
        mockMvc.perform(get("/admin/accounts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPage_okForAdmin() throws Exception {
        // render page
        when(adminService.listAccounts()).thenReturn(List.of());

        mockMvc.perform(get("/admin/accounts"))
                .andExpect(status().isOk());
    }
}

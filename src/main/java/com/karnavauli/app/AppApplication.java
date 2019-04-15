package com.karnavauli.app;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.repository.KvTableRepository;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class AppApplication implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private KvTableRepository kvTableRepository;
    private TicketRepository ticketRepository;

    public AppApplication(UserRepository userRepository, PasswordEncoder passwordEncoder, KvTableRepository kvTableRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kvTableRepository = kvTableRepository;
        this.ticketRepository = ticketRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findAll().stream().noneMatch(user -> user.getUsername().equals("bielas"))) {
            System.out.println("++++++++++++++++++++++++++++++++++");
            User user = User.builder().numberOfTickets(9999).password(passwordEncoder.encode("kvAdmin")).role(Role.CEO).username("bielas").build();
            userRepository.save(user);
        }
        if (ticketRepository.findAll().stream().noneMatch(ticketDto -> ticketDto.getFullName().equalsIgnoreCase("regular"))) {
            TicketDto ticketDto = TicketDto.builder()
                    .fullName("regular")
                    .shortName("regular")
                    .tablesDto(kvTableRepository.findAll()
                            .stream()
                            .map(table -> modelMapper().map(table, KvTableDto.class))
                            .collect(Collectors.toList()))
                    .isUni(false)
                    .build();
            Ticket ticket = ticketRepository.save(modelMapper().map(ticketDto, Ticket.class));

            List<User> users =
                    userRepository.findAll()
                            .stream()
                            .filter(user -> user.getRole().equals(Role.CEO))
                            .collect(Collectors.toList());

            users.forEach(user -> user.getTickets().add(ticket));
            userRepository.saveAll(users);
        }
    }
}

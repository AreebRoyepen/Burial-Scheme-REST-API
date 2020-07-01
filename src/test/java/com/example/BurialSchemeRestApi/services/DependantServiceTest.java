package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.dto.DependantRequestDTO;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Relationship;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import com.example.BurialSchemeRestApi.repositories.RelationshipRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class DependantServiceTest {

    @InjectMocks
    DependantService dependantService;

    @Mock
    MemberRepo memberRepo;
    @Mock
    RelationshipRepo relationshipRepo;
    @Mock
    DependantRepo dependantRepo;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void allMembers() {
    }

    @Test
    void addDependant() throws ValidationException, JsonProcessingException {

        DependantRequestDTO dependantRequestDTO = DependantRequestDTO.builder().name("Nabeel").member(1).relationship(1).build();




        when(memberRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(Member.builder().ID(1).build()));
        when(relationshipRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable((Relationship.builder().ID(1).build())));
        when(dependantRepo.findAll()).thenReturn(new ArrayList<>());
        //System.out.println(new ResponseEntity(,HttpStatus.OK).toString());

        ObjectMapper mapper = new ObjectMapper();
        //Staff staff = createStaff();
        // pretty print
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dependantService.addDependant(dependantRequestDTO));
        System.out.println(json);


    }

    @Test
    void getDependantByName() {
    }

    @Test
    void dependantLikeName() {
    }

    @Test
    void getDependantByID() {
    }

    @Test
    void deleteDependant() {
    }

    @Test
    void update() {
    }
}
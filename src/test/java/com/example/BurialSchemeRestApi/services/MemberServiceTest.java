package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.dto.DependantDTO;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Dependant;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MemberServiceTest {

    @InjectMocks
    MemberService memberService;
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
    void allMembers() throws JsonProcessingException, ValidationException {


        //DependantDTO dependantDTO = DependantDTO.builder().name("Nabeel").member(1).relationship(1).build();

        ArrayList <Member> arr = new ArrayList<>();

        List<Dependant> list = new ArrayList<>();
        list.add(Dependant.builder().ID(1).name("A").build());
        list.add(Dependant.builder().ID(2).name("B").build());

        arr.add(Member.builder().ID(1).dependants(list).build());
        //dependantService.addDependant(dependantDTO);


        //when(memberRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(Member.builder().ID(1).build()));
        when(memberRepo.findAll()).thenReturn(arr);
        //when(relationshipRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable((Relationship.builder().ID(1).build())));
        //when(dependantRepo.findAll()).thenReturn(new ArrayList<>());
        //System.out.println(new ResponseEntity(,HttpStatus.OK).toString());

        ObjectMapper mapper = new ObjectMapper();
        //Staff staff = createStaff();
        // pretty print
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(memberService.allMembers());
        System.out.println(json);

    }
}
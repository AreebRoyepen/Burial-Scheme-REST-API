package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.dto.DependantRequestDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Audit;
import com.example.BurialSchemeRestApi.models.Dependant;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Relationship;
import com.example.BurialSchemeRestApi.repositories.AuditRepo;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import com.example.BurialSchemeRestApi.repositories.RelationshipRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Service
public class DependantService {

    MemberRepo memberRepo;
    DependantRepo dependantRepo;
    RelationshipRepo relationshipRepo;
    AuditRepo auditRepo;

    public DependantService(MemberRepo memberRepo, DependantRepo dependantRepo, RelationshipRepo relationshipRepo, AuditRepo auditRepo) {
        this.memberRepo = memberRepo;
        this.dependantRepo = dependantRepo;
        this.relationshipRepo = relationshipRepo;
        this.auditRepo = auditRepo;
    }

    public ResponseMessageList allMembers() {
        return ResponseMessageList.builder().data(dependantRepo.findAll()).message(ResponseStatus.SUCCESS.name()).build();
    }

    public ResponseMessageObject myMember(Long id) throws ValidationException {

        try{

            Dependant dep = dependantRepo.findById(id).orElseThrow();
            return ResponseMessageObject.builder().data(dep.getMember()).message(ResponseStatus.SUCCESS.name()).build();

        }catch (NoSuchElementException ex){
            log.error("No such dependant");
            throw new ValidationException("No such dependant");
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw new ValidationException(ex.getMessage());
        }

    }

    public Message addDependant(DependantRequestDTO dependantRequestDTO) throws ValidationException {

        try{
            Member m = memberRepo.findById(dependantRequestDTO.getMember()).orElseThrow();
            try{
                Relationship r = relationshipRepo.findById(dependantRequestDTO.getRelationship()).orElseThrow();

                Dependant dependant = Dependant.builder()
                        .relationship(r)
                        .member(m)
                        .DOB(dependantRequestDTO.getDOB())
                        .child(dependantRequestDTO.isChild())
                        .IDNumber(dependantRequestDTO.getIDNumber())
                        .name(dependantRequestDTO.getName())
                        .surname(dependantRequestDTO.getSurname())
                        .build();

                List<Dependant> dependants = dependantRepo.findAll();

                for(Dependant x : dependants) {

                    if(	x.getName().equals(dependant.getName()) && x.getSurname().equals(dependant.getSurname())) {

                        if( x.getDOB().equals(dependant.getDOB())){
                            throw new ValidationException("Similar dependant already exists");

                        }else if(x.getMember().equals(dependant.getMember())) {
                            throw new ValidationException("Dependant already added");

                        }
                    }
                }     
                dependantRepo.save(dependant);
                auditRepo.save(Audit.builder()
                        .other(true).info("User added dep")
                        .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                        .dependant(dependant.toString())
                        .build());
                return ResponseMessageObject.builder().data(dependant).message(ResponseStatus.SUCCESS.name()).build();


            }catch (NoSuchElementException ex){
                log.error("No such relationship");
                throw new ValidationException("No such relationship");
            }catch(Exception ex){
                ex.printStackTrace();
                throw new ValidationException(ex.getMessage());
            }

        }catch (NoSuchElementException ex){
            log.error("No such Member");
            throw new ValidationException("No such Member");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }

    }


    public Message getDependantByName(String name){
        return ResponseMessageList.builder().data(dependantRepo.findByName(name)).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message dependantLikeName(String p){
        return ResponseMessageList.builder().data(dependantRepo.findByNameContains(p)).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message getDependantByID(Long id) {
        return ResponseMessageObject.builder().data(dependantRepo.findById(id).orElseThrow()).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message deleteDependant(Long id) throws ValidationException {
        try {
            Dependant dep = dependantRepo.findById(id).orElseThrow();
            dependantRepo.deleteById(dep.getID());
            auditRepo.save(Audit.builder()
                    .other(true).info("User deleted dep")
                    .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                    .dependant(dep.toString())
                    .build());
            return ResponseMessageList.builder().message(ResponseStatus.SUCCESS.name()).build();

        }catch(NoSuchElementException e) {
            log.error("Trying to delete a dependant that does not exist");
            throw new ValidationException("No such Dependant");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }
    }

    public Message update(Long id, DependantRequestDTO dependantRequestDTO) throws ValidationException {

        try {
            Dependant updateDep = dependantRepo.findById(id).orElseThrow();

            try{

                if(dependantRequestDTO.getMember() != 0L) updateDep.setMember(memberRepo.findById(dependantRequestDTO.getMember()).orElseThrow());
                if(dependantRequestDTO.getName() != null) updateDep.setName(dependantRequestDTO.getName());
                if(dependantRequestDTO.getSurname() != null) updateDep.setSurname(dependantRequestDTO.getSurname());
                if(dependantRequestDTO.getIDNumber() != null) updateDep.setIDNumber(dependantRequestDTO.getIDNumber());
                if(dependantRequestDTO.getDOB()!=null) updateDep.setDOB(dependantRequestDTO.getDOB());
                if(dependantRequestDTO.getDOE()!= null)updateDep.setDOE(dependantRequestDTO.getDOE());
                updateDep.setChild(dependantRequestDTO.isChild());

                try{
                    if(dependantRequestDTO.getRelationship() != 0)
                        updateDep.setRelationship(relationshipRepo.findById(dependantRequestDTO.getRelationship()).orElseThrow());

                    auditRepo.save(Audit.builder()
                            .other(true).info("User added dep")
                            .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                            .beforeValue(dependantRequestDTO.toString())
                            .afterValue(updateDep.toString())
                            .build());

                    return ResponseMessageObject.builder().data(dependantRepo.save(updateDep)).message(ResponseStatus.SUCCESS.name()).build();

                }catch (NoSuchElementException ex){
                    log.error("No such relationship");
                    throw new ValidationException("No such relationship");
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    throw new ValidationException(ex.getMessage());
                }

            }catch (NoSuchElementException ex){
                log.error("No such Member");
                throw new ValidationException("No such Member");
            }catch(Exception ex){
                ex.printStackTrace();
                throw new ValidationException(ex.getMessage());
            }

        }catch(NoSuchElementException e) {

            log.error("Trying to update a dependant that does not exist");
            throw new ValidationException("No such dependant");

        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }
    }

}

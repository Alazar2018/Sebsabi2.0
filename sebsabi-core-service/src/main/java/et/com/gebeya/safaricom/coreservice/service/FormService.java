package et.com.gebeya.safaricom.coreservice.service;


import et.com.gebeya.safaricom.coreservice.Exceptions.FormNotFoundException;
import et.com.gebeya.safaricom.coreservice.dto.FormDto;
import et.com.gebeya.safaricom.coreservice.dto.FormQuestionDto;
import et.com.gebeya.safaricom.coreservice.dto.JobFormDisplaydto;
import et.com.gebeya.safaricom.coreservice.model.*;
import et.com.gebeya.safaricom.coreservice.repository.FormRepository;
import et.com.gebeya.safaricom.coreservice.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    private final GigWorkerService gigWorkerService;
    private final ClientService clientService;
    private final UserResponseService userResponseService;

    @Transactional
    public JobFormDisplaydto createForm(FormDto formDto,String clientUsername) {
        Form newForm=new Form(formDto);
        Optional<Client> clientInfo=clientService.getClientByUsername(clientUsername);
            if(clientInfo.isPresent()){
                Client newClient=clientInfo.get();
                newForm.setClient(newClient);
            }
        log.info("New form created titled: {}", newForm.getTitle());
        return new JobFormDisplaydto(formRepository.save(newForm));
    }


    public Form addQuestionToForm(Long formId, FormQuestionDto questionDTO) throws InvocationTargetException, IllegalAccessException {
        Form form = getFormById(formId);

        FormQuestion question = new FormQuestion(questionDTO);

        form.getQuestions().add(question);
        question.setForm(form);

        return formRepository.save(form);
    }
    public List<Form> getAllForms() {
       return formRepository.findAll();
    }


    public Form getFormById(Long id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Form not found with id: " + id));
    }
    public Optional<Form> getFormByClientId(Long clientId) {
        Optional<Form> form = formRepository.findFormByClient_Id(clientId);
        if (form.isEmpty()) {
            throw new FormNotFoundException("Form not found for client ID: " + clientId);
        }
        return form;
    }

    public List<Form> getFormsByStatus(Status status) {
        return formRepository.findFormsByStatus(status);
    }
    public List<Form> getFormsByClientEmailAndStatus(String email, Status status) {
        return formRepository.findFormsByClient_EmailAndStatus(email, status);
    }
    public Form updateForm(Long id, FormDto formDTO) throws InvocationTargetException, IllegalAccessException {
        Form existingForm = getFormById(id);
        BeanUtils.copyProperties(formDTO, existingForm);
        return formRepository.save(existingForm);
    }


    public Form getFormForGigWorker(Long gigWorkerId, Long formId) throws AccessDeniedException {
        return formRepository.findByIdAndAssignedGigWorkerId(formId, gigWorkerId).orElseThrow(() -> new AccessDeniedException("You are not authorized to access this form"));
    }
    public UserResponse submitResponse(UserResponseDto responseDTO) {
        Form form = getFormById(responseDTO.getFormId());
        FormQuestion question = form.getQuestions().stream()
                .filter(q -> q.getId().equals(responseDTO.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found in the form"));

        GigWorker gigWorker = gigWorkerService.getGigWorkerById(responseDTO.getGigWorkerId());

        UserResponse userResponse = new UserResponse();
        userResponse.setForm(form);
        userResponse.setQuestion(question);
        userResponse.setGigWorker(gigWorker);

        // Create Answer entity and set answerText
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setAnswerText(responseDTO.getUserAnswer());

        // Set the answer in the UserResponse entity
        userResponse.setAnswers(Collections.singletonList(answer));

        return userResponseService.saveResponse(userResponse);
    }
}

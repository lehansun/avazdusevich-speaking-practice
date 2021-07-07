package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.dao.config.TestConfig;
import com.lehansun.pet.project.model.EntityStatus;
import com.lehansun.pet.project.model.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class RequestJpaDaoTest {

    @Autowired
    private RequestDao requestDao;
    @Autowired
    private CustomerDao customerDao;

    @Test
    void save() {
        // given
        Request request = getNewRequest();
        List<Request> requests = requestDao.getAll();
        Integer numberOfRecordsBefore = requests.size();

        // when
        requestDao.save(request);
        requests = requestDao.getAll();
        Integer numberOfRecordsAfter = requests.size();

        //then
        assertNotNull(request.getId());
        assertEquals(1, (numberOfRecordsAfter - numberOfRecordsBefore));
    }

    @Test
    public void getAll() {
        // when
        List<Request> requests = requestDao.getAll();

        //then
        assertNotNull(requests);
        assertTrue(requests.size() > 0);
    }

    @Test
    public void getById() {
        // given
        Request request = getNewRequest();
        requestDao.save(request);

        // when
        Optional<Request> byId = requestDao.getById(request.getId());

        // then
        assertTrue(byId.isPresent());
        assertEquals(request, byId.get());
    }

    @Test
    public void update() {
        // given
        Request request = getNewRequest();
        requestDao.save(request);
        EntityStatus status = request.getStatus();

        // when
        request.setStatus(EntityStatus.NOT_ACTIVE);
        requestDao.update(request);
        request = requestDao.getById(request.getId()).get();

        // then
        assertNotEquals(status, request.getStatus());
        assertEquals(EntityStatus.NOT_ACTIVE, request.getStatus());
    }

    @Test
    public void delete() {
        // given
        Request request = getNewRequest();
        Integer numberOfRecordsBefore = requestDao.getAll().size();


        // when
        requestDao.save(request);
        Integer numberOfRecordsAfterSaving = requestDao.getAll().size();
        requestDao.delete(request);
        Integer numberOfRecordsAfterDeleting = requestDao.getAll().size();
        Optional<Request> byId = requestDao.getById(request.getId());

        //then
        assertEquals(1, (numberOfRecordsAfterSaving - numberOfRecordsBefore));
        assertEquals(numberOfRecordsBefore, numberOfRecordsAfterDeleting);
        assertFalse(byId.isPresent());
    }

    private Request getNewRequest() {
        Request request = new Request();
        request.setInitiatedBy(customerDao.getAll().get(0));
        request.setRequestedLanguage(request.getInitiatedBy().getNativeLanguage());
        LocalDate now = LocalDate.now();
        request.setWishedStartTime(now.plusDays(1));
        request.setWishedEndTime(now.plusDays(2));
        return request;
    }

}

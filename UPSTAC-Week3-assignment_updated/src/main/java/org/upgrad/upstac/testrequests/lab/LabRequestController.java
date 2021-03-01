package org.upgrad.upstac.testrequests.lab;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/labrequests")
public class LabRequestController {

    Logger log = LoggerFactory.getLogger(LabRequestController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;

    @Autowired
    private TestRequestFlowService testRequestFlowService;


/*
    With respect to the Tester, complete the code in 'LabRequestController' class to return the list of test requests assigned to current tester. Specifically, you need to implement the "getForTester()" method.
    With respect to the Doctor, complete the code in ‘ConsultationController’ class to view all the test requests, the test requests assigned to current doctor, assign consultations to themselves and update the doctor suggestions. Specifically, you need to implement the
    "getForConsultations()", "getForDoctor()", "assignForConsultation()" and the "updateConsultation()" methods.*/

 */
    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/to-be-tested")
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTests()  {


       return testRequestQueryService.findBy(RequestStatus.INITIATED);




    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTester()  {

        // Implement This Method
        User obj = new User();
        obj = userLoggedInService.getLoggedInUser();
        List<TestRequest> res = new ArrayList<>();
        res.addAlL(testRequestQueryService.findByTester(obj));

        // Create an object of User class and store the current logged in user first
        //Implement this method to return the list of test requests assigned to current tester(make use of the above created User object)
        //Make use of the findByTester() method from testRequestQueryService class
        // For reference check the method getForTests() method from LabRequestController class

//        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,"Not implemented"); // replace this line with your code
        return res;

    }


    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForLabTest(@PathVariable Long id) {



        User tester =userLoggedInService.getLoggedInUser();

      return   testRequestUpdateService.assignForLabTest(id,tester);
    }

    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/update/{id}")
    public TestRequest updateLabTest(@PathVariable Long id,@RequestBody CreateLabResult createLabResult) {

        try {

            User tester=userLoggedInService.getLoggedInUser();
            return testRequestUpdateService.updateLabTest(id,createLabResult,tester);


        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }





}

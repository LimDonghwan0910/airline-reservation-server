//package airlineReservation.domain.admin.controller;
//
//import airlineReservation.domain.admin.service.*;
//import airlineReservation.domain.admin.serviceInput.*;
//import airlineReservation.domain.admin.serviceMapper.*;
//import airlineReservation.domain.admin.serviceOutput.*;
//import airlineReservation.infra.dto.*;
//import airlineReservation.global.constant.ApiEndpoints;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class ScheduleController {
//
//    private final CreateScheduleTemplateService createService;
//    private final CreateScheduleTemplateServiceMapper createMapper;
//    private final SearchScheduleTemplateService searchScheduleTemplateService;
//    private final SearchScheduleTemplateServiceMapper searchScheduleTemplateMapper;
//    private final SearchScheduleService searchScheduleService;
//    private final SearchScheduleServiceMapper searchScheduleMapper;
//    private final DeleteScheduleTemplateService deleteScheduleTemplateService;
//    private final DeleteScheduleTemplateServiceMapper deleteScheduleTemplateMapper;
//    private final DeleteScheduleService deleteScheduleService;
//    private final DeleteScheduleServiceMapper deleteScheduleMapper;
//
//    @PostMapping(ApiEndpoints.Admin.CREATE_SCHEDULE_TEMPLATE)
//    public ResponseEntity<CreateScheduleTemplateResponse> createScheduleTemplate(
//            @RequestBody CreateScheduleTemplateRequest request) {
//
//        CreateScheduleTemplateServiceInput serviceInput = createMapper.toServiceInput(request);
//
//        CreateScheduleTemplateServiceOutput serviceOutput = createService.create(serviceInput);
//
//        CreateScheduleTemplateResponse response = createMapper.toResponse(serviceOutput);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    @GetMapping(ApiEndpoints.Admin.SEARCH_SCHEDULE_TEMPLATE)
//    public ResponseEntity<SearchScheduleTemplateResponse> searchScheduleTemplate(
//            @ModelAttribute SearchScheduleTemplateRequest request) {
//
//        SearchScheduleTemplateServiceInput serviceInput = searchScheduleTemplateMapper.toServiceInput(request);
//
//        SearchScheduleTemplateServiceOutput serviceOutput = searchScheduleTemplateService.search(serviceInput);
//
//        SearchScheduleTemplateResponse response = searchScheduleTemplateMapper.toResponse(serviceOutput);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping(ApiEndpoints.Admin.DELETE_SCHEDULE_TEMPLATE)
//    public ResponseEntity<DeleteScheduleTemplateResponse> deleteScheduleTemplate(
//            @RequestBody DeleteScheduleTemplateRequest request) {
//
//        DeleteScheduleTemplateServiceInput serviceInput = deleteScheduleTemplateMapper.toServiceInput(request);
//
//        DeleteScheduleTemplateServiceOutput serviceOutput = deleteScheduleTemplateService.delete(serviceInput);
//
//        DeleteScheduleTemplateResponse response = deleteScheduleTemplateMapper.toResponse(serviceOutput);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping(ApiEndpoints.Admin.SEARCH_SCHEDULE)
//    public ResponseEntity<SearchScheduleResponse> searchSchedule(
//            @ModelAttribute SearchScheduleRequest request) {
//
//        SearchScheduleServiceInput serviceInput = searchScheduleMapper.toServiceInput(request);
//
//        SearchScheduleServiceOutput serviceOutput = searchScheduleService.search(serviceInput);
//
//        SearchScheduleResponse response = searchScheduleMapper.toResponse(serviceOutput);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping(ApiEndpoints.Admin.DELETE_SCHEDULE)
//    public ResponseEntity<DeleteScheduleResponse> deleteSchedule(
//            @RequestBody DeleteScheduleRequest request) {
//
//        DeleteScheduleServiceInput serviceInput = deleteScheduleMapper.toServiceInput(request);
//
//        DeleteScheduleServiceOutput serviceOutput = deleteScheduleService.delete(serviceInput);
//
//        DeleteScheduleResponse response = deleteScheduleMapper.toResponse(serviceOutput);
//
//        return ResponseEntity.ok(response);
//    }
//}
